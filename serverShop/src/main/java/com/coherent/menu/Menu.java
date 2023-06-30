package com.coherent.menu;

import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;

import java.util.Scanner;

public class Menu extends BaseMenu {
    private static Scanner scanner = new Scanner(System.in);

    public Menu(ProductService productService, CategoryService categoryService, CategoryView categoryView, ProductView productView, OrderView orderView) {
        super(productService, categoryService, categoryView, productView, orderView);
    }

    public void run() throws Exception {
        clearScreen();
        int option;
        do {
            System.out.println("Main MENU: ");
            System.out.println("1 Admin");
            System.out.println("2 Shopper");
            System.out.println("\n9 EXIT\n");
            System.out.print("Please enter your choice: ");
            option = scanner.nextInt();
            clearScreen();
            switch (option) {
                case 1 ->
                        new AdminMenu(productService, categoryService, categoryView, productView, orderView).runAdminMenu();
                case 2 ->
                        new ShopperMenu(productService, categoryService, categoryView, productView, orderView).runShopperView();
                case 9 -> {
                    System.out.println("GoodBye for now!");
                }
                default -> System.out.println("Wrong Input! Please check again the options");
            }
        } while (option != 9);
    }
}
