package com.coherent.views;

import com.coherent.common.models.Order;
import com.coherent.common.models.Product;
import com.coherent.service.OrderService;
import com.coherent.service.ProductService;
import com.coherent.threads.OrderStatusChangerToCompleted;
import com.coherent.threads.OrderStatusChangerToConfirmed;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class OrderView {
    private final ProductService productService;
    private final OrderService orderService;
    private static Scanner orderingScanner = new Scanner(System.in);

    public void launchOrderView() throws SQLException, IOException {
        boolean productFound = false;
        while (!productFound) {
            productFound = true;
            System.out.println("\nPlease enter the the name of the product. Type \"exit\" is you want to return to previous menu");
            String productName = orderingScanner.nextLine();
            if (productName.equals("exit")) {
                return;
            }
            Optional<Product> product = productService.findProductByName(productName);

            if (product.isPresent()) {
                Order order = new Order();
                order.setProduct_id(product.get().getId());
                orderService.addOrder(order);
                System.out.println("order issued");
            } else {
                System.out.println("Product " + productName + " not found!");
                productFound = false;
            }
        }
        System.in.read();
    }

    public void listOrders() {
        System.out.println(orderService.getAllOrders());
        System.out.println("\n\n\n");
    }

    public void runSimulation() {
        OrderStatusChangerToCompleted orderStatusChangerToCompleted = new OrderStatusChangerToCompleted(orderService);
        OrderStatusChangerToConfirmed orderStatusChangerToConfirmed = new OrderStatusChangerToConfirmed(orderService);
        try {
            orderStatusChangerToConfirmed.start();
            orderStatusChangerToCompleted.start();
            while (System.in.available() == 0) {
                orderService.createOrdersBulk();
                //adding orders into DB every 10 seconds
                Thread.sleep(10000);
            }
        } catch (SQLException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            orderStatusChangerToCompleted.stopThread();
            orderStatusChangerToConfirmed.stopThread();
        }
    }
}
