package org.example.constants;

import lombok.Getter;

@Getter
public enum ProductStatus {

    ACTIVE("Активный"),
    INACTIVE("Не активный");

    private String name;

    ProductStatus(String name) {
        this.name = name;
    }
}
