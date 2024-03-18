package org.example.model.product;

import java.util.Date;
import java.util.List;

import org.example.constants.ProductStatus;
import org.example.model.category.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

    private Long id;
    private String name;

    private Category category;
    private Integer price;
    private int discountPercent;
    private ProductStatus status;

    private byte[] imageData;

    private byte[] catalogImageData;
    private List<Byte> productImages;
    //@DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date lastModified;

    private String description;

}
