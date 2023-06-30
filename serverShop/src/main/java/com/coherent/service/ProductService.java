package com.coherent.service;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Product;
import com.coherent.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void addProductsToCategory(Product product, String category) throws SQLException, ParserConfigurationException, TransformerException {

        productRepository.addProductsToCategory(product, category);
    }

    public void removeProduct(String productName) throws SQLException, ParserConfigurationException, TransformerException {
        productRepository.removeProduct(productName);
    }

    public Catalog getShopCatalog() throws SQLException {
        return productRepository.getShopCatalog();
    }

    public Optional<Product> findProductByName(String productName) throws SQLException {
        return productRepository.findProductByName(productName);
    }

    public String getShopRepositoryImplementationName()
    {
        return productRepository.getClass().getName();
    }
}
