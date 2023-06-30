package com.coherent.repository;

import com.coherent.service.xmlservice.XMLParser;
import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.common.models.Shop;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class ProductRepositoryXMLImpl extends ShopRepositoryXMLImpl implements ProductRepository {

    public ProductRepositoryXMLImpl(Shop shop, XMLParser xmlParser) {
        super(shop, xmlParser);
    }

    @Override
    public void addProductsToCategory(Product product, String category) throws ParserConfigurationException, TransformerException {
        product.setId(getMaxProductId() + 1);
        for (Category searchedCategory : shop.getCatalog().getCategories()) {
            if (searchedCategory.getCategoryName().equals(category)) {
                for (Product searchedProduct : searchedCategory.getProducts()) {
                    if (searchedProduct.getName().equals(product.getName())) {
                        searchedProduct.setPrice(product.getPrice());
                        searchedProduct.setStock(searchedProduct.getStock() + product.getStock());
                        saveData();
                        return;
                    }
                }
                searchedCategory.addProductsToCategory(product);
                saveData();
                return;
            }
        }
        throw new IllegalArgumentException("Category " + category + " doesn't exist!");
    }

    @Override
    public void removeProduct(String productName) throws ParserConfigurationException, TransformerException {
        for (Category category : shop.getCatalog().getCategories()) {
            for (Product product : category.getProducts()) {
                if (product.getName().equals(productName)) {
                    category.getProducts().remove(product);
                    saveData();
                    return;
                }
            }
        }
        throw new RuntimeException("Operation failed! Product was not found");
    }

    @Override
    public Optional<Product> findProductByName(String productName) throws SQLException {
        List<Category> categories = shop.getCatalog().getCategories();

        Optional<Product> resultProduct = categories
                .stream()
                .map(Category::getProducts)
                .flatMap(List::stream)
                .filter(product -> product.getName().equals(productName))
                .findFirst();

        return resultProduct;
    }

    private int getMaxProductId() {
        List<Category> categories = shop.getCatalog().getCategories();

        return categories.stream()
                .map(Category::getProducts)
                .flatMap(Collection::stream)
                .map(Product::getId)
                .mapToInt(Integer::intValue)
                .max().orElseThrow();
    }
}
