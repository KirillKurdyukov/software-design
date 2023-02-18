package ru.itmo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import ru.itmo.actor.MasterActor;
import ru.itmo.server.Server;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("browsers");

        try (Server server = new Server()) {
            ActorRef master = system.actorOf(
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

            try {
                System.out.println(
                        ask(master, "query", Timeout.apply(10, TimeUnit.SECONDS))
                                .toCompletableFuture()
                                .get()
                );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            system.terminate();
        }
    }
}