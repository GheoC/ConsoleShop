package com.coherent.service;

import com.coherent.common.models.Order;
import com.coherent.common.models.OrderStatus;
import com.coherent.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void addOrder(Order order) throws SQLException {
        order.setOrderStatus(OrderStatus.ISSUED);
        order.setIssuedDateTime(LocalDateTime.now());
        orderRepository.addOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<Order> getOrdersFilteredByOrderStatus(OrderStatus orderStatusName) {
        return orderRepository.getOrdersFilteredByOrderStatus(orderStatusName);
    }

    public Optional<Order> findOrderById(int orderId) {
        return orderRepository.findOrderById(orderId);
    }

    public void updateOrderToConfirmed(Order order) {
        orderRepository.updateOrderByChangingOrderStatus(order, OrderStatus.CONFIRMED);
    }

    public void updateOrderToCompleted(Order order) {
        orderRepository.updateOrderByChangingOrderStatus(order, OrderStatus.COMPLETED);
    }

    //This is for this part of the task: "Provide for the possibility of placing an
    // unlimited number of orders."
    //This method simulates adding 10 Orders to the Database with status ISSUED
    // this method runs on the main() thread;
    // meanwhile on thread 1 it's running the updateOrderToConfirm() method
    // and on thread 2 it's running the updateOrderToCompleted() method
    public void createOrdersBulk() throws SQLException {
        System.out.println();
        for (int i = 1; i < 10; i++) {
            Order order = new Order();
            order.setProduct_id(1);
            addOrder(order);
        }
    }
}
