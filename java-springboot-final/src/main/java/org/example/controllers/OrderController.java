package org.example.controllers;

import org.example.daos.OrderDao;
import org.example.models.Order;
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
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public List<Order> getAll() {
        return orderDao.getOrders();
    }
    @GetMapping("/{id}")
    public Order get(@PathVariable int id) {
        Order order = orderDao.getOrderById(id);

        if (order == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found"
            );
        }

        return order;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody Order order) {
        return orderDao.createOrder(order);
    }
    @PutMapping("/{id}")
    public Order update(@PathVariable int id,
                        @RequestBody Order order) {
        if (orderDao.getOrderById(id) == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found"
            );
        }

        order.setId(id);
        return orderDao.updateOrder(order);
    }
    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        int rowsAffected = orderDao.deleteOrder(id);

        if (rowsAffected == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order not found"
            );
        }

        return rowsAffected;
    }
}
