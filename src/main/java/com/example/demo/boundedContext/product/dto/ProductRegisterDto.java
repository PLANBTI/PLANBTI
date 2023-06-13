package com.example.demo.boundedContext.product.dto;

import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.product.entity.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegisterDto {
    private Long id;

    private String category;

    private String name;

    private String content;

    private int price;

    private int salePrice;

    private int count;

    private MultipartFile file;

    public Product toEntity(Category categoryEntity){
        return Product.builder()
                .category(categoryEntity)
                .name(name)
                .content(content)
                .price(price)
                .salePrice(salePrice)
                .count(count)
                .build();
    }

    public Product update(Long id,Category categoryEntity){
        return Product.builder()
                .category(categoryEntity)
                .id(id)
                .name(name)
                .content(content)
                .price(price)
                .salePrice(salePrice)
                .count(count)
                .build();
    }



}
