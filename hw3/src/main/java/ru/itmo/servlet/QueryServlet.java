package ru.itmo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.repository.JPARepository;
import ru.itmo.utils.HtmlUtils;

import java.io.IOException;

public class QueryServlet extends HttpServlet {

    private final JPARepository repository;

    public QueryServlet(JPARepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        String html = switch (command) {
            case "max" -> HtmlUtils.htmlProduct(repository.maxProductByPrice(), "Product with max price");
            case "min" -> HtmlUtils.htmlProduct(repository.minProductByPrice(), "Product with min price");
            case "sum" -> HtmlUtils.htmlObject(repository.summaryPrice(), "Summary price");
            case "count" -> HtmlUtils.htmlObject(repository.countProducts(), "Number of products");
            default -> "Unknown command: " + command;
        };

        response.getWriter().println(html);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}