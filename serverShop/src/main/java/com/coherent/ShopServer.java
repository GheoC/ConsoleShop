package com.coherent;

import com.coherent.repository.OrderRepository;
import com.coherent.repository.OrderRepositoryH2Impl;
import com.coherent.repository.ProductRepository;
import com.coherent.repository.ProductRepositoryH2Impl;
import com.coherent.repository.databaseservice.DBConnector;
import com.coherent.server.ClientHandler;
import com.coherent.service.OrderService;
import com.coherent.service.ProductService;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ShopServer {

    public static void main(String[] args) throws SQLException, IOException {
        String jdbcURL = "jdbc:h2:" + "./src/main/resources/Database/MyShop";
        DBConnector dbConnector = new DBConnector(jdbcURL);
        ProductRepository productRepository = new ProductRepositoryH2Impl(dbConnector);
        OrderRepository orderRepository = new OrderRepositoryH2Impl(dbConnector);
        ProductService productService = new ProductService(productRepository);
        OrderService orderService = new OrderService(orderRepository);
        ServerSocket serverListener = new ServerSocket(200);

        while (true)
        {
            System.out.println("Server started! Waiting for a client ...");
            Socket client = serverListener.accept();
            System.out.println("Client connected");
            ClientHandler clientThread = new ClientHandler(client, productService, orderService);
            clientThread.start();
        }
    }
}
