package com.coherent.menu;

import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;
import com.coherent.views.StatisticsView;

import java.util.Scanner;

public class StatisticsMenu extends BaseMenu {
    private static Scanner statisticsScanner = new Scanner(System.in);
    private final StatisticsView statisticsView;

    public StatisticsMenu(ProductService productService, CategoryService categoryService, CategoryView categoryView, ProductView productView, OrderView orderView, StatisticsView statisticsView) {
        super(productService, categoryService, categoryView, productView, orderView);
        this.statisticsView = statisticsView;
    }


    public void run() throws Exception {

        int option;

        do {
            clearScreen();
            System.out.println("Statistics MENU: ");
            System.out.println("1 Sort products by general sorting rule: (Price - Ascending; Stock - Descending; Name - Ascending;)");
            System.out.println("2 Sort products by one property");
            System.out.println("3 Display the sun of the most expressive 5 products from each category");

            System.out.println("\n9 EXIT\n");
            System.out.print("Please enter your choice: ");
            option = statisticsScanner.nextInt();
            clearScreen();

            switch (option) {
                case 1 -> {
                    statisticsView.displayResultForSortingByGeneralRule();
                    System.in.read();
                }
                case 2 -> {
                    statisticsView.displayCustomSortView();
                    System.in.read();
                }
                case 3 -> {
                    statisticsView.displayFiveMostExpensiveProducts();
                    System.in.read();
                }
                case 9 -> System.out.println("Goodbye");
                default -> System.out.println("Wrong choice! Please check again the options");
            }
        } while (option != 9);
    }
}
