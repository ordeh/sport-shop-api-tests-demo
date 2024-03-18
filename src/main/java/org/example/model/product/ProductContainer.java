package org.example.model.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductContainer {

    private List<Product> products;
    private long totalCount;
    private Long defaultCategoryId;

    public ProductContainer(long totalCount) {
        this.totalCount = totalCount;
    }
}
