package ru.itmo.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.itmo.repository.JPARepository;
import ru.itmo.servlet.AddProductServlet;
import ru.itmo.servlet.GetProductsServlet;
import ru.itmo.servlet.QueryServlet;

import java.util.Properties;

public class ServerWeb {
    private final Server server;
    private final JPARepository jpaRepository;

    public ServerWeb(Server server, Properties properties) {
        this.server = server;
        this.jpaRepository = new JPARepository(properties);
    }

    public void start() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(jpaRepository)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(jpaRepository)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(jpaRepository)), "/query");

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
