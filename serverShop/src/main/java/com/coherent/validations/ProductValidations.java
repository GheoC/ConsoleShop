package com.coherent.validations;

import com.coherent.common.models.Product;

public class ProductValidations {

    public static void validateProduct(Product product) {
        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("Stock must me above 0");
        }

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must me above 0");
        }
    }

    public static Double validateProductPrice(Double price) {
        if (price <= 0 || price == null) {
            throw new IllegalArgumentException("Price must be above 0");
        }


        return price;
    }

    public static int validateProductStock(Integer stock) {
        if (stock <= 0 || stock == null) {
            throw new IllegalArgumentException("Stock must me above 0");
        }
        return stock;
    }


}
