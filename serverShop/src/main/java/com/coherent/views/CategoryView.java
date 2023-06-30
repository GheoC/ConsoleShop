package com.coherent.views;

import com.coherent.common.models.Category;
import com.coherent.service.CategoryService;
import lombok.AllArgsConstructor;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

@AllArgsConstructor
public class CategoryView {
    private final CategoryService categoryService;
    private static final Scanner scanner = new Scanner(System.in);

    public void addCategoryView() throws IOException {

        boolean isCategoryNameValid = false;
        while (!isCategoryNameValid) {
            isCategoryNameValid = true;

            System.out.println("\nAdding category. Type \"exit\" is you want to return to previous menu. ");
            String categoryName = readCategoryName();

            if (categoryName.equals("exit")) {
                return;
            }

            Category category = new Category();
            category.setCategoryName(categoryName);

            try {
                categoryService.addCategory(category);
                System.out.println("\nCategory " + categoryName + " was added successfully in catalog!");
            } catch (SQLException | ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                isCategoryNameValid = false;
            }
            System.in.read();
        }
    }

    public void removeCategoryView() throws IOException {
        boolean isCategoryNameValid = false;
        while (!isCategoryNameValid) {
            isCategoryNameValid = true;

            System.out.println("\nRemoving category. Type \"exit\" is you want to return to previous menu.");
            String categoryName = readCategoryName();

            if (categoryName.equals("exit")) {
                return;
            }

            try {
                categoryService.removeCategory(categoryName);
                System.out.println("\nCategory " + categoryName + " was removed successfully in catalog!");
            } catch (SQLException | ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                isCategoryNameValid = false;
            }
            System.in.read();
        }
    }

    private String readCategoryName() {
        System.out.print("Please enter category name: ");
        return scanner.nextLine();
    }
}
