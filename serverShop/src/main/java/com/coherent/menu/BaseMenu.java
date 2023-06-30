package com.coherent.menu;

import com.coherent.common.models.Catalog;
import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;
import java.sql.SQLException;

public class BaseMenu {
    protected final ProductService productService;
    protected final CategoryService categoryService;
    protected final CategoryView categoryView;
    protected final ProductView productView;
    protected final OrderView orderView;

    public BaseMenu(ProductService productService, CategoryService categoryService, CategoryView categoryView, ProductView productView, OrderView orderView) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.categoryView = categoryView;
        this.productView = productView;
        this.orderView = orderView;
    }

    public void clearScreen() {
        for (int i = 0; i < 150; i++) {
            System.out.println();
        }
    }
    public void printShopProducts() throws SQLException {
        Catalog catalog = productService.getShopCatalog();
        System.out.println(catalog);
    }
}
