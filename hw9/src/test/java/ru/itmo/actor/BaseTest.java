package ru.itmo.actor;

import akka.actor.ActorSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    protected ActorSystem system;

    @BeforeEach
    public void initActorSystem() {
        system = ActorSystem.create("browsers");
    }

    @AfterEach
    public void terminateActorSystem() {
        system.terminate();
    }
}
