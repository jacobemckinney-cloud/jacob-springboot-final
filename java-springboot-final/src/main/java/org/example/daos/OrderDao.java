package org.example.daos;
import org.example.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Order> getOrders() {
        return jdbcTemplate.query(
                "SELECT * FROM orders ORDER BY id;",
                this::mapToOrder
        );
    }
    public Order getOrderById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM orders WHERE id = ?;",
                    this::mapToOrder,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (username) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, order.getUsername());
            return statement;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        return getOrderById(newId);
    }
    public Order updateOrder(Order order) {
        String sql = "UPDATE orders SET username = ? WHERE id = ?;";

        jdbcTemplate.update(
                sql,
                order.getUsername(),
                order.getId()
        );

        return getOrderById(order.getId());
    }
    public int deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }
    private Order mapToOrder(ResultSet resultSet, int rowNumber)
            throws SQLException {
        return new Order(
                resultSet.getInt("id"),
                resultSet.getString("username")
        );
    }
}
