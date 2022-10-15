package ru.itmo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.model.Product;
import ru.itmo.repository.JPARepository;

import java.io.IOException;

public class AddProductServlet extends HttpServlet {

    private final JPARepository repository;

    public AddProductServlet(JPARepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        repository.insertProduct(new Product(
                name,
                price
        ));

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}