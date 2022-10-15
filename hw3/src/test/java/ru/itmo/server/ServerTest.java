package ru.itmo.server;

import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>()
            .withPassword("123qweqwe")
            .withUsername("kurdyukov-kir")
            .withInitScript("migration/schema.sql");

    private static final HttpClient client = HttpClient.newHttpClient();
    private static ServerWeb serverWeb;

    @BeforeAll
    public static void startServer() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("database.url", postgreSQLContainer.getJdbcUrl());
        properties.setProperty("database.username", "kurdyukov-kir");
        properties.setProperty("database.password", "123qweqwe");

        serverWeb = new ServerWeb(new Server(12345), properties);
        serverWeb.start();
    }

    @AfterAll
    public static void stopServer() throws Exception {
        serverWeb.stop();
    }

    @Test
    @Order(1)
    public void addingProductTest() throws URISyntaxException, IOException, InterruptedException {
        var request = createRequest("http://localhost:12345/add-product?name=iphone6&price=300");

        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());

        request = createRequest("http://localhost:12345/add-product?name=tomato&price=1000");

        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
    }

    @Test
    @Order(2)
    public void checkProductsTest() throws URISyntaxException, IOException, InterruptedException {
        assertByUrlAndBody(
                "http://localhost:12345/get-products",
                "<html><body>iphone6\t300</br>tomato\t1000</br></body></html>"
        );
    }

    @Test
    @Order(3)
    public void checkQueryTest() throws URISyntaxException, IOException, InterruptedException {
        assertByUrlAndBody(
                "http://localhost:12345/query?command=max",
                "<html><body><h1>Product with max price: </h1>tomato\t1000</br></body></html>"
        );

        assertByUrlAndBody(
                "http://localhost:12345/query?command=min",
                "<html><body><h1>Product with min price: </h1>iphone6\t300</br></body></html>"
        );
        assertByUrlAndBody(
                "http://localhost:12345/query?command=sum",
                "<html><body>Summary price: 1300</body></html>"
        );

        assertByUrlAndBody(
                "http://localhost:12345/query?command=count",
                "<html><body>Number of products: 2</body></html>"
        );
    }

    private void assertByUrlAndBody(String uri, String body) throws URISyntaxException, IOException, InterruptedException {
        var requestMax = createRequest(uri);

        HttpResponse<String> response = client.send(requestMax, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        Assertions.assertEquals(
                body + "\n",
                response.body()
        );
    }

    private HttpRequest createRequest(String uri) throws URISyntaxException {
        return HttpRequest
                .newBuilder(new URI(uri))
                .GET()
                .build();
    }
}
