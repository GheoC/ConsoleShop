package com.coherent.client;

import com.coherent.common.models.Order;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Scanner;

@RequiredArgsConstructor
public class ClientMenu {
    private static Scanner clientScanner = new Scanner(System.in);
    private final ClientService clientService;

    public void runClientMenu() throws IOException, ClassNotFoundException {
        clearScreen();
        int option;
        do {
            System.out.println("Main MENU: ");
            System.out.println("1 Products");
            System.out.println("2 Order products");
            System.out.println("3 Display orders");
            System.out.println("\n9 EXIT\n");
            System.out.print("Please enter your choice: ");
            option = clientScanner.nextInt();
            clearScreen();
            switch (option) {
                case 1 -> {
                    System.out.println("The products are... ");
                    System.out.println(clientService.getCatalog());

                    System.in.read();
                }
                case 2 -> {
                    System.out.println("Please input the name of the product you want to order: ");
                    Scanner scanner = new Scanner(System.in);
                    String productName = scanner.nextLine();
                    String response = clientService.orderProduct(productName);
                    System.out.println(response);
                    System.in.read();
                }
                case 3 -> {
                    System.out.println("here are the orders: ");
                    System.out.println(clientService.getOrders());
                    System.in.read();
                }
                case 9 -> {
                    System.out.println("GoodBye for now!");
                    clientService.closeConnection();
                }
                default -> System.out.println("Wrong Input! Please check again the options");
            }
        } while (option != 9);
    }

    private void clearScreen() {
        for (int i = 0; i < 150; i++) {
            System.out.println();
        }
    }

}
