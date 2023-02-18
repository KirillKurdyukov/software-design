package ru.itmo.server;

import com.sun.net.httpserver.HttpServer;
import ru.itmo.actor.ChildActor;
import ru.itmo.model.Result;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.List;

public class Server implements AutoCloseable {

    private final HttpServer server;

    public Server() throws IOException {
        this(0);
    }

    public Server(int timeout) throws IOException {

        this.server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        server.createContext("/", exchange -> {
            try {
                Thread.sleep(timeout * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            var query = exchange.getRequestHeaders().get("query").get(0);

            byte[] response = ChildActor.MAPPER.writeValueAsBytes(new Result(
                    List.of(query + "-1", query + "-2")
            ));

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });

        server.start();
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
