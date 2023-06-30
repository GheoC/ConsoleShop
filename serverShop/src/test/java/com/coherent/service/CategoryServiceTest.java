package com.coherent.service;

import com.coherent.common.models.Product;
import com.coherent.repository.CategoryRepository;
import com.coherent.repository.CategoryRepositoryXMLImpl;
import com.coherent.service.sortingservices.ProductSortingService;
import com.coherent.service.sortingservices.SortingParameter;
import com.coherent.service.xmlservice.SortingParameterReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        products.add(new Product(1, "a", 50, 1));
        products.add(new Product(2, "b", 45, 1));
        products.add(new Product(3, "c", 40, 1));
        products.add(new Product(4, "d", 35, 1));
        products.add(new Product(5, "e", 30, 1));
        products.add(new Product(6, "g", 30, 1));
        products.add(new Product(7, "h", 15, 1));
    }

    @Test
    @DisplayName("Calculating the sum of prices for the 5 most expensive products")
    void getMapForSumOfMostExpensiveFiveProducts() throws Exception {
        //GIVEN
        ProductSortingService productSortingService = mock(ProductSortingService.class);
        CategoryRepository categoryRepository = mock(CategoryRepositoryXMLImpl.class);
        SortingParameterReader sortingParameterReader = mock(SortingParameterReader.class);

        CategoryService categoryService = new CategoryService(categoryRepository, productSortingService, sortingParameterReader);

        Map<String, List<Product>> categoryMap = new HashMap<>();
        categoryMap.put("groceries", products);

        //WHEN
        when(productSortingService.getProductsByCategoriesSortedBy(List.of(new SortingParameter("price", "desc")))).thenReturn(categoryMap);
        Map<String, Double> sumOfFivePrices = categoryService.getMapForSumOfMostExpensiveFiveProducts();

        //THEN
        double valueForGroceries = sumOfFivePrices.get("groceries");
        assertEquals(valueForGroceries, 200);
    }
}