package com.coherent.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements Serializable {
    private Catalog catalog;

    public void addCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

}
