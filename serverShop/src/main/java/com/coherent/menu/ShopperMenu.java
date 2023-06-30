package com.coherent.menu;

import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;

import java.util.Scanner;

public class ShopperMenu extends BaseMenu
{
    private static Scanner shopperScanner = new Scanner(System.in);
    public ShopperMenu(ProductService productService, CategoryService categoryService, CategoryView categoryView, ProductView productView, OrderView orderView) {
        super(productService, categoryService, categoryView, productView, orderView);
    }
    public void runShopperView() throws Exception
    {
        int option;
        clearScreen();
        do {
            System.out.println("Shopper MENU: ");
            System.out.println("1 Launch Order");
            System.out.println("2 List Orders");
            System.out.println("3 Simulate bulk order creation and threads running");
            System.out.println("\n9 EXIT\n");
            System.out.print("Please enter your choice: ");
            option = shopperScanner.nextInt();
            clearScreen();

            switch (option) {
                case 1 -> {
                    printShopProducts();
                    orderView.launchOrderView();
                    clearScreen();
                }
                case 2-> orderView.listOrders();
                case 3-> orderView.runSimulation();
                case 9 -> System.out.println("Goodbye");
                default -> System.out.println("Wrong choice! Please check again the options");
            }
        } while (option != 9);
    }


}
