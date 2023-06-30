package com.coherent.client;

import java.io.IOException;
import java.net.Socket;

public class ClientShop {

    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 200);
        )
        {
            ClientService clientService = new ClientService(socket);
            new ClientMenu(clientService).runClientMenu();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            System.out.println("Something went wrong... " + e.getMessage());
        }
    }
}
