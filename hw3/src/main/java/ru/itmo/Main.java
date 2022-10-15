package ru.itmo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.itmo.repository.JPARepository;
import ru.itmo.servlet.AddProductServlet;
import ru.itmo.servlet.GetProductsServlet;
import ru.itmo.servlet.QueryServlet;


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

        JPARepository jpaRepository = new JPARepository(properties);

//        Server server = new Server(8081);
//
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//
//        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
//        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
//        context.addServlet(new ServletHolder(new QueryServlet()), "/query");
//
//        server.start();
//        server.join();
    }
}