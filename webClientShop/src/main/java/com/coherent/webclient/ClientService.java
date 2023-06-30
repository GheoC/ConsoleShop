package com.coherent.webclient;

import com.coherent.common.models.Catalog;

import java.io.*;
import java.net.Socket;


public class ClientService
{
    private DataOutputStream messageToServer;
    private ObjectInputStream objectReceiver;

    public void connect(String host, int port) throws IOException
    {
        Socket socket = new Socket(host,port);
        this.messageToServer = new DataOutputStream(socket.getOutputStream());
        this.objectReceiver = new ObjectInputStream(socket.getInputStream());
    }

    public Catalog getCatalog() throws IOException, ClassNotFoundException
    {
        messageToServer.writeUTF("products");
        return (Catalog) objectReceiver.readObject();
    }

    public void closeConnection() throws IOException
    {
        messageToServer.writeUTF("finish");
    }
}
