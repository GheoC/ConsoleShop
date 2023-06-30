package com.coherent.repository;

import com.coherent.common.models.Order;
import com.coherent.common.models.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    public Optional<Order> findOrderById(int orderId);

    public void addOrder(Order order);

    public List<Order> getAllOrders();

    public List<Order> getOrdersFilteredByOrderStatus(OrderStatus orderStatusName);

    public void updateOrderByChangingOrderStatus(Order order, OrderStatus newOrderStatus);
}
