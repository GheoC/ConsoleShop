package com.coherent.service;

import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.CategoryRepository;
import com.coherent.service.sortingservices.ProductSortingService;
import com.coherent.service.sortingservices.SortingParameter;
import com.coherent.service.xmlservice.SortingParameterReader;
import lombok.RequiredArgsConstructor;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CategoryService
{
    private final CategoryRepository categoryRepository;
    private final ProductSortingService productSortingService;
    private final SortingParameterReader sortingParameterReader;

    public void addCategory(Category category) throws SQLException, IOException, ParserConfigurationException, TransformerException {
        categoryRepository.addCategory(category);
    }

    public void removeCategory(String categoryName) throws SQLException, ParserConfigurationException, TransformerException {

        categoryRepository.removeCategory(categoryName);
    }

    public Map<String, List<Product>> sortByGeneralRule() throws Exception {
        return productSortingService.getProductsByCategoriesSortedBy(sortingParameterReader.readParametersConfigFile());
    }

    public Map<String, List<Product>> getProductsByCategoriesSortedBy(String fieldName, String strategy) throws Exception {
        return productSortingService.getProductsByCategoriesSortedBy(List.of(new SortingParameter(fieldName, strategy)));
    }

    public Map<String, Double> getMapForSumOfMostExpensiveFiveProducts() throws Exception {
        Map<String, List<Product>> categoryMap = getProductsByCategoriesSortedBy("price", "desc");

        return categoryMap.entrySet().stream()
                .map(entry ->
                {
                    String categoryKey = entry.getKey();
                    List<Product> products = entry.getValue();
                    double sumOfProductPrices = products.stream().limit(5).map(Product::getPrice).mapToDouble(Double::doubleValue).sum();
                    return Map.entry(categoryKey, sumOfProductPrices);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
