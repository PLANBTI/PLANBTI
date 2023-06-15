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

    private Integer price;

    private Integer salePrice;

    private Integer count;

    private MultipartFile file;

    private String url;

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




}