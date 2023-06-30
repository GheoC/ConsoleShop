package com.coherent.views;

import com.coherent.common.models.Product;
import com.coherent.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StatisticsView {
    private final Scanner textScanner = new Scanner(System.in);
    private final CategoryService categoryService;

    public StatisticsView(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    public void displayResultForSortingByGeneralRule() throws Exception {
        Map<String, List<Product>> categoryMap = categoryService.sortByGeneralRule();

        printSortedCategoryMap(categoryMap);
    }

    public void displayCustomSortView() throws Exception {
        String fieldName = readFieldName();
        String strategy = readStrategy();
        Map<String, List<Product>> categoryMap = categoryService.getProductsByCategoriesSortedBy(fieldName, strategy);

        printSortedCategoryMap(categoryMap);
    }

    private void printSortedCategoryMap(Map<String, List<Product>> categoryMap) {
        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            System.out.println("\nCategory: " + entry.getKey() + " has the following products:");
            if (entry.getValue().isEmpty()) {
                System.out.println("No products in this category!");
            } else {
                System.out.println(entry.getValue());
            }
        }
    }

    public void displayFiveMostExpensiveProducts() throws Exception {

        Map<String, Double> sumOfProductPricesMappedToCategory = categoryService.getMapForSumOfMostExpensiveFiveProducts();

        for (Map.Entry<String, Double> categorySumPrices : sumOfProductPricesMappedToCategory.entrySet()) {
            System.out.print("\nCategory: " + categorySumPrices.getKey() + " -> ");

            if (categorySumPrices.getValue() == null) {
                System.out.println("There no products to add up!");
                break;
            }
            System.out.println("Sum of the most expensive 5 products is: " + categorySumPrices.getValue());
        }
    }

    private String readFieldName() {
        boolean isValid = false;
        String fieldName = "";
        do {
            isValid = true;
            System.out.println("\nPlease enter the sorting field (price, name or stock). Pick one");
            try {
                fieldName = textScanner.nextLine();
                if (!fieldName.equals("price") && !fieldName.equals("name") && !fieldName.equals("stock")) {
                    throw new IllegalArgumentException("Wrong field! Check the required fields");
                }
            } catch (IllegalArgumentException e) {
                isValid = false;
                System.out.println(e.getMessage());
            }
        } while (!isValid);
        return fieldName;
    }

    private String readStrategy() {
        boolean isValid = false;
        String strategyName = "";
        do {
            isValid = true;
            System.out.println("\nPlease enter the sorting strategy (asc for Ascending or desc for Descending). Pick one:");
            try {
                strategyName = textScanner.nextLine();
                if (!strategyName.equals("asc") && !strategyName.equals("desc")) {
                    throw new IllegalArgumentException("Wrong strategy! Check the required strategy");
                }
            } catch (IllegalArgumentException e) {
                isValid = false;
                System.out.println(e.getMessage());
            }
        } while (!isValid);
        return strategyName;
    }


}
