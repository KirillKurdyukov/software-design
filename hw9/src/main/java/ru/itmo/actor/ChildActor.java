package ru.itmo.actor;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itmo.exceptions.NoCorrectMessageException;
import ru.itmo.model.Result;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class ChildActor extends UntypedActor {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String url;


    public ChildActor(String url) {
        this.url = url;
    }

    @Override
    public void onReceive(Object message) throws InterruptedException, URISyntaxException, IOException {
        if (message instanceof String response) {
            var res = httpClient.send(
                    HttpRequest
                            .newBuilder()
                            .uri(new URI(url))
                            .header("query", response)
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            sender().tell(MAPPER.readValue(res.body(), Result.class), self());
            getContext().stop(self());
        } else {
            throw new NoCorrectMessageException();
        }
    }
}
