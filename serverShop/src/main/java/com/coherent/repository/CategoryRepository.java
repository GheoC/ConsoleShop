package com.coherent.repository;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.util.Optional;

public interface CategoryRepository
{
    public Catalog getShopCatalog() throws SQLException;

    public void addCategory(Category category) throws SQLException, ParserConfigurationException, TransformerException;

    public void removeCategory(String categoryName) throws SQLException, ParserConfigurationException, TransformerException;

    public Optional<Category> findCategoryByName(String categoryName) throws SQLException;

}
