package org.example.daos;

import org.example.models.OrderItem;
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
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<OrderItem> getOrderItems() {
        return jdbcTemplate.query(
                "SELECT * FROM order_items ORDER BY id;",
                this::mapToOrderItem
        );
    }
    public OrderItem getOrderItemById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM order_items WHERE id = ?;",
                    this::mapToOrderItem,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public OrderItem createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            return statement;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        return getOrderItemById(newId);
    }
    public OrderItem updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?;";

        jdbcTemplate.update(
                sql,
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getId()
        );

        return getOrderItemById(orderItem.getId());
    }
    public int deleteOrderItem(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }
    private OrderItem mapToOrderItem(ResultSet resultSet, int rowNumber)
            throws SQLException {
        return new OrderItem(
                resultSet.getInt("id"),
                resultSet.getInt("order_id"),
                resultSet.getInt("product_id"),
                resultSet.getInt("quantity")
        );
    }
}
