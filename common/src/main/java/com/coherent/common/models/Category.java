package com.coherent.common.models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Category implements Serializable {
    private int categoryId;
    private String categoryName;
    private List<Product> products = new ArrayList<>();

    public void addProductsToCategory(Product product) {
        this.products.add(product);
    }

    public void addProductsToCategory(List<Product> products) {
        this.products.addAll(products);
    }

    @Override
    public String toString() {
        return "\n         " + categoryName + "" +
                "\n               Products: " +
                "" + products;
    }
}
