package com.coherent.threads;

import com.coherent.common.models.Order;
import com.coherent.common.models.OrderStatus;
import com.coherent.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@RequiredArgsConstructor
public class OrderStatusChangerToConfirmed extends Thread {
    private final OrderService orderService;

    //Part of task: - It checks the list of orders in the system every second
    private int threadTimer = 1000;
    // Part of task: Each issued order changes its status from ISSUED to CONFIRMED from 1 to 60 seconds
    // I set it for min 1 second and maxim of 3 seconds to speed it up
    private int orderConfirmTime = 2000;
    private boolean exit = false;

    public void run() {
        Logger logger = Logger.getLogger("logThreadConfirmed");
        try {
            FileHandler fileHandler = new FileHandler("./src/main/resources/ThreadLogs/confirmedThread.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!exit) {
            Random random = new Random();
            List<Order> ordersIssued = orderService.getOrdersFilteredByOrderStatus(OrderStatus.ISSUED);
            logger.info("Orders found in Database with status ISSUED: " + ordersIssued.toString());
            System.out.println();
            ordersIssued.stream()
                    .forEach(order ->
                    {
                        orderConfirmTime = 1000 + random.nextInt(orderConfirmTime);
                        try {
                            sleep(orderConfirmTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        orderService.updateOrderToConfirmed(order);
                        logger.info("Order no. " + order.getId() + " => status changed from " + order.getOrderStatus() + " to " + OrderStatus.CONFIRMED);
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
