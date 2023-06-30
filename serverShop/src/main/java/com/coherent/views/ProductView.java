package com.coherent.views;

import com.coherent.common.models.Product;
import com.coherent.service.ProductService;
import com.coherent.validations.ProductValidations;
import lombok.AllArgsConstructor;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

@AllArgsConstructor
public class ProductView {
    private static Scanner intScanner = new Scanner(System.in);
    private static Scanner textScanner = new Scanner(System.in);
    private final ProductService productService;

    public void addProductView() throws IOException {
        boolean isValid = false;
        while (!isValid) {
            isValid = true;
            System.out.println("\nAdding product. Type \"exit\" is you want to return to previous menu");

            String productName = readProductName();
            if (productName.equals("exit")) {
                return;
            }

            double productPrice = readPrice();
            int productStock = readStock();
            String category = readCategoryName();

            Product product = new Product();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setStock(productStock);


            try {
                ProductValidations.validateProduct(product);
                productService.addProductsToCategory(product, category);
                System.out.println("\nProduct " + productName + " added successfully in " + category + "!");
            } catch (IllegalArgumentException | SQLException | ParserConfigurationException | TransformerException e) {
                System.out.println(product);
                System.out.println("!ERROR: " + e.getMessage());
                System.out.println("\n\n\n");
                isValid = false;
            } catch (RuntimeException e)
            {
                isValid = false;
                System.out.println(e.getMessage());
            }
            System.in.read();
        }
    }

    public void removeProduct() throws IOException {
        boolean isDeleted = false;
        do {
            isDeleted = true;
            System.out.println("\nRemoving a product; Type \"exit\" is you want to return to previous menu");

            String productName = readProductName();
            if (productName.equals("exit")) {
                return;
            }
            try {
                productService.removeProduct(productName);
                System.out.println("\nProduct " + productName + " was successfully deleted");
            } catch (SQLException | ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                isDeleted = false;
            }
        } while (!isDeleted);
        System.in.read();
    }

    private static String readProductName() {
        System.out.print("Please enter product name: ");
        return textScanner.nextLine();
    }

    private static double readPrice() {
        boolean isPriceValid;
        double productPrice = 0;
        do {
            isPriceValid = true;
            System.out.print("Please enter product price: ");
            try {
                double price = new Scanner(System.in).nextDouble();
                productPrice = ProductValidations.validateProductPrice(price);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                isPriceValid = false;
            } catch (InputMismatchException e) {
                System.out.println("Price must be a number");
                isPriceValid = false;
            }
        } while (!isPriceValid);
        return productPrice;
    }

    private static int readStock() {
        boolean isStockValid;
        int productStock = 0;
        do {
            isStockValid = true;
            System.out.print("Please enter product stock: ");
            try {
                int stock = intScanner.nextInt();
                productStock = ProductValidations.validateProductStock(stock);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                isStockValid = false;
            } catch (InputMismatchException e) {
                System.out.println("Stock must be a number");

            }
        } while (!isStockValid);

        return productStock;
    }

    private static String readCategoryName() {
        System.out.print("In which category: ");
        String category = textScanner.nextLine();
        return category;
    }

}
