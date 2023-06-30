package com.coherent.common.models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Catalog implements Serializable {
    private int catalogId;
    private String catalogName;
    private List<Category> categories = new ArrayList<>();


    public void addCategoryToCatalog(Category category)
    {
        categories.add(category);
    }
    public void addCategoriesToCatalog(List<Category> categories)
    {
        this.categories.addAll(categories);
    }

    public boolean hasCategory(String category) {
        for (Category categoryIterator : categories) {
            if (categoryIterator.getCategoryName().equals(category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nCatalog " + catalogName +
                ":" + categories;
    }
}
