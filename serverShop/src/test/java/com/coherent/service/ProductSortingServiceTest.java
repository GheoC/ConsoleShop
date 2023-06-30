package com.coherent.service;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.ProductRepository;
import com.coherent.repository.ProductRepositoryXMLImpl;
import com.coherent.service.sortingservices.ProductSortingService;
import com.coherent.service.sortingservices.SortingParameter;
import com.coherent.service.xmlservice.SortingParameterReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductSortingServiceTest {
    private List<Product> products;

    private List<SortingParameter> sortingParameters;

    private Catalog catalog = new Catalog();

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        products.add(new Product(1, "apple", 50, 10));
        products.add(new Product(2, "cherry", 45, 30));
        products.add(new Product(3, "kiwi", 40, 30));
        products.add(new Product(4, "ananas", 35, 20));
        products.add(new Product(5, "pear", 40, 20));
        products.add(new Product(6, "carrot", 30, 55));
        products.add(new Product(7, "orange", 15, 16));
        products.add(new Product(8, "onion", 15, 10));
        products.add(new Product(4, "plum", 35, 20));

        Category category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("groceries");
        category.setProducts(products);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        catalog.addCategoriesToCatalog(categories);

        sortingParameters = new ArrayList<>();
        sortingParameters.add(new SortingParameter("price", "asc"));
        sortingParameters.add(new SortingParameter("stock", "desc"));
        sortingParameters.add(new SortingParameter("name", "asc"));
    }

    @Test
    @DisplayName("Test the third Comparator (Name) while Price and Stock are equal")
    void sortByGeneralXMLRules_FocusOnThirdComparatorName() throws Exception {
        //GIVEN
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        //WHEN
        when(productRepository.getShopCatalog()).thenReturn(catalog);
        when(sortingParameterReader.readParametersConfigFile()).thenReturn(sortingParameters);

        //THEN
        Map<String, List<Product>> sortedCategoryProducts = productSortingService.getProductsByCategoriesSortedBy(sortingParameters);
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");
        assertEquals("ananas", groceryProducts.get(3).getName());
        assertEquals("kiwi", groceryProducts.get(5).getName());
    }

    @Test
    @DisplayName("Test the second Comparator (Stock) while first sort criteria (Price) is equal")
    void sortByGeneralXMLRules_FocusOnSecondComparatorStock() throws Exception {
        //GIVEN
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        //WHEN
        when(productRepository.getShopCatalog()).thenReturn(catalog);
        when(sortingParameterReader.readParametersConfigFile()).thenReturn(sortingParameters);

        //THEN
        Map<String, List<Product>> sortedCategoryProducts = productSortingService.getProductsByCategoriesSortedBy(sortingParameters);
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");
        assertEquals("orange", groceryProducts.get(0).getName());
    }

    @Test
    @DisplayName("Test the first Comparator Price")
    void sortByGeneralXMLRules_FocusOnFirstComparatorPrice() throws Exception {
        //GIVEN
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        //WHEN
        when(productRepository.getShopCatalog()).thenReturn(catalog);
        when(sortingParameterReader.readParametersConfigFile()).thenReturn(sortingParameters);

        //THEN
        Map<String, List<Product>> sortedCategoryProducts = productSortingService.getProductsByCategoriesSortedBy(sortingParameters);
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");
        assertEquals("cherry", groceryProducts.get(7).getName());
    }

    @Test
    @DisplayName("Sort ascending by name ")
    void sortAscendingByName() throws Exception {
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        when(productRepository.getShopCatalog()).thenReturn(catalog);

        Map<String, List<Product>> sortedCategoryProducts =
                productSortingService.getProductsByCategoriesSortedBy(List.of(new SortingParameter("name", "asc")));
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");

        assertEquals("cherry", groceryProducts.get(3).getName());
    }

    @Test
    @DisplayName("Sort descending by price ")
    void sordDescendingByPrice() throws Exception {
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        when(productRepository.getShopCatalog()).thenReturn(catalog);

        Map<String, List<Product>> sortedCategoryProducts =
                productSortingService.getProductsByCategoriesSortedBy(List.of(new SortingParameter("price", "desc")));
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");

        assertEquals("apple", groceryProducts.get(0).getName());
    }

    @Test
    @DisplayName("Sort descending by stock ")
    void sordDescendingByStock() throws Exception {
        ProductRepository productRepository = mock(ProductRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);

        when(productRepository.getShopCatalog()).thenReturn(catalog);

        Map<String, List<Product>> sortedCategoryProducts =
                productSortingService.getProductsByCategoriesSortedBy(List.of(new SortingParameter("stock", "desc")));
        List<Product> groceryProducts = sortedCategoryProducts.get("groceries");
        assertEquals("carrot", groceryProducts.get(0).getName());
    }
}