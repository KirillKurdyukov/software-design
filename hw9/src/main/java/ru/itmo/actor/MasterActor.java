package ru.itmo.actor;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.pf.DeciderBuilder;
import ru.itmo.exceptions.NoCorrectMessageException;
import ru.itmo.model.Result;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MasterActor extends UntypedActor {

    private static final int TIMEOUT = 1;

    private final Map<String, String> browsers;

    public final List<Result> result = new ArrayList<>();

    private ActorRef parentSender;

    public MasterActor(Map<String, String> browsers) {
        this.browsers = browsers;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(false, DeciderBuilder
                .match(NoCorrectMessageException.class, e -> OneForOneStrategy.restart())
                .build());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String query) {
            parentSender = sender();

            result.clear();

            for (var browser : browsers.entrySet()) {
                var refBrowser = getContext()
                        .actorOf(
                                Props.create(
                                        ChildActor.class,
                                        browser.getValue()
                                ),
                                browser.getKey()
                        );

                refBrowser.tell(query, self());
            }

            getContext().setReceiveTimeout(Duration.create(TIMEOUT, TimeUnit.SECONDS));
        } else if (message instanceof Result r) {
            result.add(r);

            if (result.size() == browsers.size()) {
                sendResult();
            }
        } else if (message instanceof ReceiveTimeout) {
            sendResult();
        } else {
            throw new NoCorrectMessageException();
        }
    }

    private void sendResult() {
        parentSender.tell(
                result.stream()
                        .flatMap(it -> it.links().stream())
                        .collect(Collectors.joining(", ")),
                self()
        );

        getContext().stop(self());
    }
}
