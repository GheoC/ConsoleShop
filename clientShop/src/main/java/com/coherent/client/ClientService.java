package com.coherent.client;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Order;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientService
{
    private DataInputStream messageFromServer;
    private DataOutputStream messageToServer;
    private ObjectInputStream objectReceiver;
    private final Socket socket;

    public ClientService(Socket socket) throws IOException {
        this.socket = socket;
        this.messageFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.messageToServer = new DataOutputStream(socket.getOutputStream());
        this.objectReceiver = new ObjectInputStream(socket.getInputStream());
    }
    public Catalog getCatalog() throws IOException, ClassNotFoundException
    {
        messageToServer.writeUTF("products");
        return (Catalog) objectReceiver.readObject();
    }
    public List<Order> getOrders() throws IOException, ClassNotFoundException {
        messageToServer.writeUTF("orders");
        return (List<Order>) objectReceiver.readObject();
    }

    public String orderProduct(String productName) throws IOException, ClassNotFoundException {
        messageToServer.writeUTF("order: "+productName);
        return (String) messageFromServer.readUTF();
    }

    public void closeConnection() throws IOException {
        messageToServer.writeUTF("finish");
    }
}
