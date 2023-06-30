package com.coherent.menu;

import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;
import com.coherent.views.StatisticsView;

import java.util.Scanner;
public class AdminMenu extends BaseMenu {
    private static Scanner adminMenuScanner = new Scanner(System.in);

    public AdminMenu(ProductService productService, CategoryService categoryService, CategoryView categoryView, ProductView productView, OrderView orderView) {
        super(productService, categoryService, categoryView, productView, orderView);
    }
    public void runAdminMenu() throws Exception {
        int option;
        clearScreen();
        do {
            System.out.println("Admin MENU: ");
            System.out.println("1 Add Products");
            System.out.println("2 Remove Products");
            System.out.println("3 View Products");
            System.out.println("4 Add Category");
            System.out.println("5 Remove Category! (Warning: It will also remove all the products from that category)");
            System.out.println("6 Statistics");
            System.out.println("\n9 EXIT\n");
            System.out.print("Please enter your choice: ");
            option = adminMenuScanner.nextInt();
            clearScreen();

            switch (option) {
                case 1 -> {
                    printShopProducts();
                    productView.addProductView();
                    clearScreen();
                }
                case 2 -> {
                    printShopProducts();
                    productView.removeProduct();
                    clearScreen();
                }
                case 3 -> {
                    clearScreen();
                    printShopProducts();
                    System.in.read();
                }
                case 4 -> {
                    printShopProducts();
                    categoryView.addCategoryView();
                    clearScreen();
                }
                case 5 -> {
                    printShopProducts();
                    categoryView.removeCategoryView();
                }
                case 6 ->
                        new StatisticsMenu(productService, categoryService, categoryView, productView, orderView, new StatisticsView(categoryService)).run();
                case 9 -> System.out.println("Goodbye");
                default -> System.out.println("Wrong choice! Please check again the options");
            }
        } while (option != 9);
    }
}
