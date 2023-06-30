package com.coherent.repository;

import com.coherent.common.models.Category;
import com.coherent.repository.databaseservice.DBConnector;

import java.sql.*;
import java.util.Optional;


public class CategoryRepositoryH2Impl extends ShopRepositoryH2Impl implements CategoryRepository {

    public CategoryRepositoryH2Impl(DBConnector dbConnector) {
        super(dbConnector);
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        Optional<Category> categoryFromDB = findCategoryByName(category.getCategoryName());
        if (categoryFromDB.isPresent()) {
            throw new RuntimeException(category.getCategoryName() + " is already present in DB");
        }
        insertCategoryInDatabase(category);
    }

    @Override
    public void removeCategory(String categoryName) throws SQLException {
        Category category = findCategoryByName(categoryName).orElseThrow(() -> new RuntimeException("Category " + categoryName + " doesn't exist in database"));
        deleteProductsOfACategory(category.getCategoryId());
        deleteCategoryFromDatabase(categoryName);
    }

    private void insertCategoryInDatabase(Category category) throws SQLException {
        String query = "insert into categories (category_name, catalog_id) values (?,?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteProductsOfACategory(int categoryId) throws SQLException {
        String query = "delete from products where category_id = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
    }

    private void deleteCategoryFromDatabase(String categoryName) throws SQLException {
        String query = "delete from categories where category_name = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            preparedStatement.setString(1, categoryName);
            preparedStatement.executeUpdate();
        }
    }
}
