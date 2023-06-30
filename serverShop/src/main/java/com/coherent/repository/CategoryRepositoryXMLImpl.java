package com.coherent.repository;

import com.coherent.service.xmlservice.XMLParser;
import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;
import com.coherent.common.models.Shop;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryXMLImpl extends ShopRepositoryXMLImpl implements CategoryRepository {

    public CategoryRepositoryXMLImpl(Shop shop, XMLParser xmlParser) {
        super(shop, xmlParser);
    }

    @Override
    public Catalog getShopCatalog() {
        return shop.getCatalog();
    }

    @Override
    public void addCategory(Category category) throws ParserConfigurationException, TransformerException {
        category.setCategoryId(getMaxIndexForCategories() + 1);
        for (Category categoryFromDB : shop.getCatalog().getCategories()) {
            if (category.getCategoryName().equals(categoryFromDB.getCategoryName())) {
                throw new RuntimeException("Category already exists in Catalog");
            }
        }
        shop.getCatalog().addCategoryToCatalog(category);
        saveData();
    }

    @Override
    public void removeCategory(String categoryName) throws ParserConfigurationException, TransformerException {
        for (Category category : shop.getCatalog().getCategories()) {
            if (category.getCategoryName().equals(categoryName)) {
                shop.getCatalog().getCategories().remove(category);
                saveData();
                return;
            }
        }
        throw new RuntimeException(categoryName + " was not found in Catalog");
    }

    @Override
    public Optional<Category> findCategoryByName(String categoryName) throws SQLException {
        List<Category> categories = shop.getCatalog().getCategories();

        return categories.stream()
                .filter(category -> category.getCategoryName().equals(categoryName))
                .findFirst();
    }

    private int getMaxIndexForCategories() {
        List<Category> categories = shop.getCatalog().getCategories();

        return categories.stream()
                .map(Category::getCategoryId)
                .mapToInt(Integer::intValue)
                .max().orElseThrow();
    }
}
