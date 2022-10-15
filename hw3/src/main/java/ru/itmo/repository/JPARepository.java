package ru.itmo.repository;

import java.util.Properties;

public class JPARepository {
    private final String url;
    private final String username;
    private final String password;

    public JPARepository(Properties properties) {
        this.url = properties.getProperty("database.url");
        this.username = properties.getProperty("database.username");
        this.password = properties.getProperty("database.password");
    }
}
