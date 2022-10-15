package ru.itmo;

import org.eclipse.jetty.server.Server;
import ru.itmo.server.ServerWeb;

import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(
                new FileInputStream(
                        Objects.requireNonNull(
                                Main.class
                                        .getClassLoader()
                                        .getResource("application.properties")
                        ).getFile()
                )
        );

        new ServerWeb(new Server(8081), properties).start();
    }
}