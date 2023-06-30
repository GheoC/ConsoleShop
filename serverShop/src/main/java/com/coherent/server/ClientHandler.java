package com.coherent.server;

import com.coherent.common.models.Order;
import com.coherent.common.models.Product;
import com.coherent.service.OrderService;
import com.coherent.service.ProductService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

public class ClientHandler extends Thread {
    private final Socket client;
    private final ProductService productService;
    private final OrderService orderService;
    private DataInputStream messageFromClient;
    private DataOutputStream messageToClient;
    private ObjectOutputStream objectSender;

    public ClientHandler(Socket clientSocket, ProductService productService, OrderService orderService) throws IOException {
        this.client = clientSocket;
        this.productService = productService;
        this.orderService = orderService;
        this.messageFromClient = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        this.messageToClient = new DataOutputStream(client.getOutputStream());
        this.objectSender = new ObjectOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String request = "";
            while (!request.equals("finish"))
            {
                request = messageFromClient.readUTF();
                if (request.equals("products")) {
                    objectSender.writeObject(productService.getShopCatalog());
                }
                if (request.equals("orders")) {
                    objectSender.writeObject(orderService.getAllOrders());
                }
                if (request.contains("order:")) {
                    issueOrder(request);
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                System.out.println("Client "+ client.getInetAddress()+ " disconnected!");
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void issueOrder(String request) throws SQLException, IOException {
        String productName = request.replace("order: ", "");
        Optional<Product> product = productService.findProductByName(productName);
        if (product.isPresent()) {
            Order order = new Order();
            order.setProduct_id(product.get().getId());
            orderService.addOrder(order);
            messageToClient.writeUTF("order issued! ");
        } else {
            messageToClient.writeUTF("order failed! ");
        }
    }
}
