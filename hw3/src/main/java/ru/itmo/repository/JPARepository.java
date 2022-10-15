package ru.itmo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JPARepository {
    private static final Logger logger = LoggerFactory.getLogger(JPARepository.class);

    private final String url;
    private final String username;
    private final String password;

    public JPARepository(Properties properties) {
        this.url = properties.getProperty("database.url");
        this.username = properties.getProperty("database.username");
        this.password = properties.getProperty("database.password");
    }

    public void insertProduct(Product product) {
        try (Connection connection = connection()) {
            PreparedStatement statement = connection
                    .prepareStatement("insert into Product (name, product) values (?, ?);");

            statement.setString(1, product.name());
            statement.setInt(2, product.price());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Fail insert new product.", e);

            throw new RuntimeException(e);
        }
    }

    public List<Product> findAllProducts() {
        try (Connection connection = connection()) {
            PreparedStatement statement = connection
                    .prepareStatement("select * from Product;");

            return fetchListProductFromSql(statement);
        } catch (SQLException e) {
            logger.error("Fail find all products.", e);

            throw new RuntimeException(e);
        }
    }

    public Product maxProductByPrice() {
        try (Connection connection = connection()) {
            PreparedStatement statement = connection
                    .prepareStatement("select * from Product order by price desc limit 1;");

            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        } catch (SQLException e) {
            logger.error("Fail find max product by price.", e);

            throw new RuntimeException(e);
        }
    }

    public Product minProductByPrice() {
        try (Connection connection = connection()) {
            PreparedStatement statement = connection
                    .prepareStatement("select * from Product order by price limit 1;");

            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        } catch (SQLException e) {
            logger.error("Fail find min product by price.", e);

            throw new RuntimeException(e);
        }
    }

    public int countProducts() {
        try (Connection connection = connection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from Product;");
            resultSet.next();

            int res = resultSet.getInt(1);

            resultSet.close();
            statement.close();

            return res;
        } catch (SQLException e) {
            logger.error("Fail counting products.", e);

            throw new RuntimeException(e);
        }
    }

    public int summaryPrice() {
        try (Connection connection = connection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select sum(price) from Product;");
            resultSet.next();

            int res = resultSet.getInt(1);

            resultSet.close();
            statement.close();

            return res;
        } catch (SQLException e) {
            logger.error("Fail summing products.", e);

            throw new RuntimeException(e);
        }
    }

    private List<Product> fetchListProductFromSql(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(
                    new Product(
                            resultSet.getString("name"),
                            resultSet.getInt("price")
                    )
            );
        }

        resultSet.close();
        statement.close();

        return products;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
