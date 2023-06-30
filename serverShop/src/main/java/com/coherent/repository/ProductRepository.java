package com.coherent.repository;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Product;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.Optional;

public interface ProductRepository
{
    public Catalog getShopCatalog() throws SQLException;

    public void addProductsToCategory(Product product, String category) throws SQLException, ParserConfigurationException, TransformerException;

    public void removeProduct(String productName) throws SQLException, ParserConfigurationException, TransformerException;

    public Optional<Product> findProductByName(String productName) throws SQLException;
}
