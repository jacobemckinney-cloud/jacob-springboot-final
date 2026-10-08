package org.example.controllers;
import org.example.daos.OrderItemDao;
import org.example.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemDao orderItemDao;

    @GetMapping
    public List<OrderItem> getAll() {
        return orderItemDao.getOrderItems();
    }
    @GetMapping("/{id}")
    public OrderItem get(@PathVariable int id) {
        OrderItem orderItem = orderItemDao.getOrderItemById(id);

        if (orderItem == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found"
            );
        }

        return orderItem;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItem create(@RequestBody OrderItem orderItem) {
        return orderItemDao.createOrderItem(orderItem);
    }
    @PutMapping("/{id}")
    public OrderItem update(@PathVariable int id,
                            @RequestBody OrderItem orderItem) {
        if (orderItemDao.getOrderItemById(id) == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found"
            );
        }

        orderItem.setId(id);
        return orderItemDao.updateOrderItem(orderItem);
    }
    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderItemDao.deleteOrderItem(id);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item not found"
            );
        }

        return rowsAffected;
    }
}
