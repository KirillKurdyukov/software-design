package ru.itmo.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.server.Server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class MasterActorTest extends BaseTest {

    private ActorRef master;

    @BeforeEach
    public void createMaster() {
        master = system.actorOf(
                Props.create(
                        MasterActor.class,
                        Map.of(
                                "Yandex", "http://localhost:8080",
                                "Google", "http://localhost:8080",
                                "Bing", "http://localhost:8080"
                        )
                ),
                "master"
        );
    }

    @Test
    public void testWithoutServerTimeout() throws IOException {
        try(Server server = new Server()) {
            String result = (String) ask(master, "query", Timeout.apply(1, TimeUnit.SECONDS))
                    .toCompletableFuture()
                    .join() ;

            Assertions.assertEquals("query-1, query-2, query-1, query-2, query-1, query-2", result);
        }
    }

    @Test
    public void testWithServerTimeout() throws IOException {
        try(Server server = new Server(2)) {
            String result = (String) ask(master, "query", Timeout.apply(3, TimeUnit.SECONDS))
                    .toCompletableFuture()
                    .join() ;

            Assertions.assertEquals("", result);
        }
    }
}
