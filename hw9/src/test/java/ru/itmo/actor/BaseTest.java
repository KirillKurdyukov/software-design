package ru.itmo.actor;

import akka.actor.ActorSystem;

public abstract class BaseTest {
    protected static final ActorSystem system = ActorSystem.create("browsers");
}
