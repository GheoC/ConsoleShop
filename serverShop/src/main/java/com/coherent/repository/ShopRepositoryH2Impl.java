package com.coherent.repository;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.databaseservice.DBConnector;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class ShopRepositoryH2Impl {
    protected final DBConnector dbConnector;

    public Catalog getShopCatalog() {
        Catalog catalog = new Catalog();
        String query = "select id, catalog_name from catalogs";

        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);)
        {
            Map<Integer, List<Category>> categoriesMappedToCatalog = getCategoriesMappedToCatalog();
            ResultSet catalogResultSet = statement.executeQuery(query);
            while (catalogResultSet.next()) {
                int catalogId = Integer.parseInt(catalogResultSet.getString("id"));
                String catalogName = catalogResultSet.getString("catalog_name");
                catalog.setCatalogId(catalogId);
                catalog.setCatalogName(catalogName);
                if (categoriesMappedToCatalog.containsKey(catalogId)) {
                    catalog.addCategoriesToCatalog(categoriesMappedToCatalog.get(catalogId));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return catalog;
    }

    private Map<Integer, List<Category>> getCategoriesMappedToCatalog() throws SQLException {
        String query = "select id, category_name, catalog_id from categories";
        Map<Integer, List<Category>> categoriesMappedToCatalog = new HashMap<>();

        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);)
        {
            ResultSet categoriesResultSet = statement.executeQuery(query);
            Map<Integer, List<Product>> productsMappedToCategory = getProductsMappedTpCategory();

            while (categoriesResultSet.next()) {
                Category category = new Category();
                int categoryId = Integer.parseInt(categoriesResultSet.getString("id"));
                String categoryName = categoriesResultSet.getString("category_name");
                category.setCategoryId(categoryId);
                category.setCategoryName(categoryName);

                if (productsMappedToCategory.containsKey(categoryId)) {
                    List<Product> productsForThisCategory = productsMappedToCategory.get(categoryId);
                    category.addProductsToCategory(productsForThisCategory);
                }
                int currentCategoryCatalogId = Integer.parseInt(categoriesResultSet.getString("catalog_id"));

                if (!categoriesMappedToCatalog.containsKey(currentCategoryCatalogId)) {
                    List<Category> categoriesForThisCatalog = new ArrayList<>();
                    categoriesForThisCatalog.add(category);
                    categoriesMappedToCatalog.put(currentCategoryCatalogId, categoriesForThisCatalog);
                } else {
                    categoriesMappedToCatalog.get(currentCategoryCatalogId).add(category);
                }
            }
        }
        return categoriesMappedToCatalog;
    }

    private Map<Integer, List<Product>> getProductsMappedTpCategory() throws SQLException {
        String query = "select id, product_name, price, stock, category_id from products";
        Map<Integer, List<Product>> productsMappedToCategory = new HashMap<>();

        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            ResultSet productResultSet = statement.executeQuery(query);
            while (productResultSet.next()) {
                int id = Integer.parseInt(productResultSet.getString("id"));
                String name = productResultSet.getString("product_name");
                double price = Double.parseDouble(productResultSet.getString("price"));
                int stock = Integer.parseInt(productResultSet.getString("stock"));
                Product product = new Product(id, name, price, stock);

                int currentProductCategoryId = Integer.parseInt(productResultSet.getString("category_id"));
                if (!productsMappedToCategory.containsKey(currentProductCategoryId)) {
                    List<Product> productsForThisCategory = new ArrayList<>();
                    productsForThisCategory.add(product);
                    productsMappedToCategory.put(currentProductCategoryId, productsForThisCategory);
                } else {
                    productsMappedToCategory.get(currentProductCategoryId).add(product);
                }
            }
        }
        return productsMappedToCategory;
    }

    public Optional<Category> findCategoryByName(String categoryName) throws SQLException {
        String query = "select * from categories where category_name = ?";
        Category category = null;

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            preparedStatement.setString(1, categoryName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                category = new Category();
                int id = resultSet.getInt("id");
                String name = resultSet.getString("category_name");
                category.setCategoryId(id);
                category.setCategoryName(name);
            }
        }
        return Optional.ofNullable(category);
    }
}
