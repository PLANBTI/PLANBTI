package com.example.demo.boundedContext.product.dto;

import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.product.entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
public class ProductDto {
    private Long id;
    private Category category;
    private String name;
    private String content;
    private int price;
    private int salePrice;
    private int count;
    private String imageUrl;

    public ProductDto(Product product, int count) {
        this.id = product.getId();
        this.category = product.getCategory();
        this.name = product.getName();
        this.content = product.getContent();
        this.price = product.getPrice();
        this.salePrice = product.getSalePrice();
        this.count = count;
        this.imageUrl = product.getImageUrl();
    }

    public void addCount(int count) {
        this.count += count;
    }
}