package com.coherent.repository;

import com.coherent.common.models.Order;
import com.coherent.common.models.OrderStatus;
import com.coherent.repository.databaseservice.DBConnector;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderRepositoryH2Impl implements OrderRepository {
    protected final DBConnector dbConnector;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");

    @Override
    public Optional<Order> findOrderById(int orderId) {
        String query = "select id, product_id, order_status, issued_date, confirmed_date, completed_date " +
                "from orders where id = ?";
        Order order = null;
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("product_id");
                String orderStatus = resultSet.getString("order_status");
                String issuedDateString = resultSet.getString("issued_date");
                String confirmedDateString = resultSet.getString("confirmed_date");
                String completedDateString = resultSet.getString("completed_date");
                order = new Order();
                order.setId(orderId);
                order.setProduct_id(productId);
                order.setOrderStatus(OrderStatus.valueOf(orderStatus));
                if (issuedDateString != null) {
                    LocalDateTime issuedDateTime = LocalDateTime.parse(issuedDateString, dateTimeFormatter);
                    order.setIssuedDateTime(issuedDateTime);
                }
                if (confirmedDateString != null) {
                    LocalDateTime confirmedDateTime = LocalDateTime.parse(confirmedDateString, dateTimeFormatter);
                    order.setConfirmedDateTime(confirmedDateTime);
                }
                if (completedDateString != null) {
                    LocalDateTime completedDateTime = LocalDateTime.parse(completedDateString, dateTimeFormatter);
                    order.setCompletedDateTime(completedDateTime);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(order);
    }

    @Override
    public void addOrder(Order order) {
        String query = "insert into orders (product_id, order_status, issued_date) values (?,?,?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, order.getProduct_id());
            preparedStatement.setString(2, String.valueOf(order.getOrderStatus()));
            String stringDate = order.getIssuedDateTime().format(dateTimeFormatter);
            preparedStatement.setString(3, stringDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        String query = "select id, product_id, order_status, issued_date, confirmed_date, completed_date from orders";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            ResultSet ordersResultSet = statement.executeQuery(query);
            while (ordersResultSet.next()) {
                int orderId = ordersResultSet.getInt("id");
                int productId = ordersResultSet.getInt("product_id");
                String orderStatus = ordersResultSet.getString("order_status");
                String issuedDateString = ordersResultSet.getString("issued_date");
                String confirmedDateString = ordersResultSet.getString("confirmed_date");
                String completedDateString = ordersResultSet.getString("completed_date");

                Order order = new Order();
                order.setId(orderId);
                order.setProduct_id(productId);
                order.setOrderStatus(OrderStatus.valueOf(orderStatus));
                if (issuedDateString != null) {
                    LocalDateTime issuedDateTime = LocalDateTime.parse(issuedDateString, dateTimeFormatter);
                    order.setIssuedDateTime(issuedDateTime);
                }
                if (confirmedDateString != null) {
                    LocalDateTime confirmedDateTime = LocalDateTime.parse(confirmedDateString, dateTimeFormatter);
                    order.setConfirmedDateTime(confirmedDateTime);
                }
                if (completedDateString != null) {
                    LocalDateTime completedDateTime = LocalDateTime.parse(completedDateString, dateTimeFormatter);
                    order.setCompletedDateTime(completedDateTime);
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersFilteredByOrderStatus(OrderStatus orderStatusName) {
        String query = "";
        if (orderStatusName == OrderStatus.ISSUED) {
            query = "select id, product_id, order_status, issued_date, confirmed_date, completed_date " +
                    "from orders where order_status = 'ISSUED'";
        }
        if (orderStatusName == OrderStatus.CONFIRMED) {
            query = "select id, product_id, order_status, issued_date, confirmed_date, completed_date " +
                    "from orders where order_status = 'CONFIRMED'";
        }
        if (orderStatusName == OrderStatus.COMPLETED) {
            query = "select id, product_id, order_status, issued_date, confirmed_date, completed_date " +
                    "from orders where order_status = 'COMPLETED'";
        }
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            ResultSet ordersResultSet = statement.executeQuery(query);
            while (ordersResultSet.next()) {
                int orderId = ordersResultSet.getInt("id");
                int productId = ordersResultSet.getInt("product_id");
                String orderStatus = ordersResultSet.getString("order_status");
                String issuedDateString = ordersResultSet.getString("issued_date");
                String confirmedDateString = ordersResultSet.getString("confirmed_date");
                String completedDateString = ordersResultSet.getString("completed_date");

                Order order = new Order();
                order.setId(orderId);
                order.setProduct_id(productId);
                order.setOrderStatus(OrderStatus.valueOf(orderStatus));
                if (issuedDateString != null) {
                    LocalDateTime issuedDateTime = LocalDateTime.parse(issuedDateString, dateTimeFormatter);
                    order.setIssuedDateTime(issuedDateTime);
                }
                if (confirmedDateString != null) {
                    LocalDateTime confirmedDateTime = LocalDateTime.parse(confirmedDateString, dateTimeFormatter);
                    order.setConfirmedDateTime(confirmedDateTime);
                }
                if (completedDateString != null) {
                    LocalDateTime completedDateTime = LocalDateTime.parse(completedDateString, dateTimeFormatter);
                    order.setCompletedDateTime(completedDateTime);
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public void updateOrderByChangingOrderStatus(Order order, OrderStatus newOrderStatus) {
        if (!((order.getOrderStatus() == OrderStatus.ISSUED && newOrderStatus == OrderStatus.CONFIRMED) ||
                (order.getOrderStatus() == OrderStatus.CONFIRMED && newOrderStatus == OrderStatus.COMPLETED))) {
            throw new RuntimeException("Cannot change the status for order to " + newOrderStatus + "! Please check the current status of order! ");
        }

        String query = "";
        if (order.getOrderStatus() == OrderStatus.ISSUED) {
            query = "update orders set order_status = ?, confirmed_date = ? where id = ?";
        }

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            query = "update orders set order_status = ?, completed_date = ? where id = ?";
        }
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, String.valueOf(newOrderStatus));
            String stringDate = LocalDateTime.now().format(dateTimeFormatter);
            preparedStatement.setString(2, stringDate);
            preparedStatement.setInt(3, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
