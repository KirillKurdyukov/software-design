package ru.itmo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.model.Product;
import ru.itmo.repository.JPARepository;
import ru.itmo.utils.HtmlUtils;

import java.io.IOException;
import java.util.List;

public class GetProductsServlet extends HttpServlet {

    private final JPARepository repository;

    public GetProductsServlet(JPARepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> productList = repository.findAllProducts();
        response.getWriter().println(HtmlUtils.htmlProducts(productList));
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}