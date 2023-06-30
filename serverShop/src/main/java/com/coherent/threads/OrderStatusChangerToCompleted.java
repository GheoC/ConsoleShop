package com.coherent.threads;

import com.coherent.common.models.Order;
import com.coherent.common.models.OrderStatus;
import com.coherent.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@RequiredArgsConstructor
public class OrderStatusChangerToCompleted extends Thread {
    private final OrderService orderService;
    //Part of task: - it checks whether the execution of orders (in my case that means change status to COMPLETED)
    //has been completed every two seconds.
    private int threadTimer = 2000;
    private boolean exit = false;

    public void run() {
        Logger logger = Logger.getLogger("logThreadCompleted");
        try {
            FileHandler fileHandler = new FileHandler("./src/main/resources/ThreadLogs/completedThread.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (!exit) {
            System.out.println();
            List<Order> ordersConfirmed = orderService.getOrdersFilteredByOrderStatus(OrderStatus.CONFIRMED);
            logger.info("Orders found in Database with status CONFIRMED : "+ ordersConfirmed.toString());
            ordersConfirmed.stream()
                    .forEach(order ->
                    {
                        orderService.updateOrderToCompleted(order);
                        logger.info("Order no. " + order.getId() + " => status changed from " + order.getOrderStatus() + " to " + OrderStatus.COMPLETED);
                    });
            try {
                sleep(threadTimer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopThread() {
        exit = true;
    }
}
