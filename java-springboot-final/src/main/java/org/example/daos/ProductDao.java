package org.example.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import org.example.models.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);


    }
    public List<Product> getProducts() {
        return jdbcTemplate.query(
                "SELECT * FROM products ORDER BY id;",
                this::mapToProduct
        );
    }
    public Product getProductById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM products WHERE id = ?;",
                    this::mapToProduct,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            return statement;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        return getProductById(newId);
    }
    public Product updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?;";

        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getId()
        );

        return getProductById(product.getId());
    }
    public int deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }
    private Product mapToProduct(ResultSet resultSet, int rowNumber)
            throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getBigDecimal("price")
        );
    }
}
