package com.coherent.repository;

import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.databaseservice.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductRepositoryH2Impl extends ShopRepositoryH2Impl implements ProductRepository {
    public ProductRepositoryH2Impl(DBConnector dbConnector) {
        super(dbConnector);
    }

    @Override
    public void addProductsToCategory(Product product, String categoryName) throws SQLException {
        //Scenario: category is not present in DB =>throw exception
        Category categoryFromDB = findCategoryByName(categoryName).orElseThrow(() -> new RuntimeException("Category doesn't exist in database"));
        Optional<Product> productFromDB = findProductByName(product.getName());
        Optional<Integer> categoryId = getCategoryIdForProduct(product.getName());

        //Scenario: product exist in DB, but on different category => throw exception
        if (productFromDB.isPresent() && (categoryFromDB.getCategoryId() != categoryId.get())) {
            throw new RuntimeException("Product exist in Database, but in a different category!");
        }
        //Scenario: the product is not present in DB => we insert a new product into DB;
        if (!productFromDB.isPresent()) {
            insertProductInDatabase(product, categoryFromDB.getCategoryId());
        }
        //Scenario: the product is present in DB => we update the price and increase the stock;
        if (productFromDB.isPresent()) {
            int newStock = productFromDB.get().getStock() + product.getStock();
            product.setStock(newStock);
            updateProductFromDatabase(product);
        }
    }

    @Override
    public void removeProduct(String productName) throws SQLException {
        Optional<Product> productFromDB = findProductByName(productName);
        if (!productFromDB.isPresent()) {
            throw new RuntimeException("Product " + productName + " is not present in Database");
        }

        deleteProductFromDatabase(productName);
    }

    @Override
    public Optional<Product> findProductByName(String productName) throws SQLException {
        String query = "select * from products where product_name = ?";
        Product product = null;
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                product = new Product(id, name, price, stock);
            }
        }
        return Optional.ofNullable(product);
    }

    private Optional<Integer> getCategoryIdForProduct(String productName) throws SQLException {
        String query = "select category_id from products where product_name = ?";
        Integer categoryId = null;
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categoryId = resultSet.getInt("category_id");
            }
        }
        return Optional.ofNullable(categoryId);
    }

    private void insertProductInDatabase(Product product, int categoryId) throws SQLException {
        String query = "insert into products (product_name, price, stock, category_id) values (?,?,?,?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getStock());
            preparedStatement.setInt(4, categoryId);
            preparedStatement.executeUpdate();
        }
    }

    private void updateProductFromDatabase(Product product) throws SQLException {
        String query = "update products set price=?, stock=? where product_name = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setDouble(1, product.getPrice());
            preparedStatement.setInt(2, product.getStock());
            preparedStatement.setString(3, product.getName());
            preparedStatement.executeUpdate();
        }
    }

    private void deleteProductFromDatabase(String productName) throws SQLException {
        String query = "delete from products where product_name = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            preparedStatement.setString(1, productName);
            preparedStatement.executeUpdate();
        }
    }


}
