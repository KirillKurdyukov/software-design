package ru.itmo.actor;

import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itmo.model.Result;
import ru.itmo.server.Server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class ChildActorTest extends BaseTest {

    @Test
    public void successSendMessageChildActor() {
        try (Server server = new Server()) {
            var child = system.actorOf(
                    Props.create(ChildActor.class, "http://localhost:8080"),
                    "child"
            );

            Result result = (Result) ask(child, "request", Timeout.apply(10, TimeUnit.SECONDS))
                    .toCompletableFuture()
                    .join();

            Assertions.assertEquals(2, result.links().size());
            Assertions.assertEquals(List.of("request-1", "request-2"), result.links());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
