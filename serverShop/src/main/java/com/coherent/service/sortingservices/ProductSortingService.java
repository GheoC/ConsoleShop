package com.coherent.service.sortingservices;

import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductSortingService {
    private final ProductRepository productRepository;

    public Map<String, List<Product>> getProductsByCategoriesSortedBy(List<SortingParameter> sortingParams) throws Exception {
        List<Category> categories = productRepository.getShopCatalog().getCategories();
        Comparator<Product> customComparator = buildComparator(sortingParams);

        return categories.stream()
                .collect(
                        Collectors.toMap(
                                Category::getCategoryName,
                                category -> category.getProducts()
                                        .stream()
                                        .sorted(customComparator)
                                        .collect(Collectors.toList()))
                );
    }

    private Comparator<Product> buildComparator(List<SortingParameter> sortingParameterList) throws Exception {
        Comparator comparator = createCustomComparator(sortingParameterList.get(0).getParameterField(), sortingParameterList.get(0).getStrategy());

        for (SortingParameter sortingParameter : sortingParameterList) {
            String fieldName = sortingParameter.getParameterField();
            String strategy = sortingParameter.getStrategy();
            comparator = comparator.thenComparing(createCustomComparator(fieldName, strategy));
        }
        return comparator;
    }

    private Comparator<Product> createCustomComparator(String fieldName, String strategy) throws ClassNotFoundException, NoSuchMethodException {
        if (strategy.equals("desc")) {
            return Comparator.comparing(createCustomFunction(fieldName)).reversed();
        }
        if (strategy.equals("asc")) {
            return Comparator.comparing(createCustomFunction(fieldName));
        }
        return null;
    }

    private Function<Product, ? extends Comparable<Object>> createCustomFunction(String fieldName) throws ClassNotFoundException, NoSuchMethodException {
        String methodName = getMethodFromField(fieldName);
        Method method = Class.forName("com.coherent.common.models.Product").getMethod(methodName);
        return
                product -> {
                    try {
                        return (Comparable<Object>) method.invoke(product);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
    }

    private String getMethodFromField(String field) {
        return "get"
                + field.substring(0, 1).toUpperCase()
                + field.substring(1);
    }
}


