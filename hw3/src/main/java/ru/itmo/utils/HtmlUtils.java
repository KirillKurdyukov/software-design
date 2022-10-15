package ru.itmo.utils;

import ru.itmo.model.Product;

import java.util.List;

public class HtmlUtils {
    private HtmlUtils() {

    }

    public static String htmlProducts(List<Product> productList) {
        return htmlProducts(productList, null);
    }

    public static String htmlProducts(List<Product> productList, String header) {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><body>");
        if (header != null) {
            builder.append("<h1>");
            builder.append(header);
            builder.append("</h1>");
        }

        productList.forEach(product -> builder
                .append(product.name())
                .append("\t")
                .append(product.price())
                .append("</br>")
        );

        builder.append("</body></html>");
        return builder.toString();
    }

    public static String htmlObject(Object object, String header) {
        return "<html><body>" +
                header +
                object.toString() +
                "</body></html>";
    }
}
