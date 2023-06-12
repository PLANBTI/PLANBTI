package com.example.demo.boundedContext.product.dto;

import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.product.entity.Product;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private Category category;

    private String name;

    private String content;

    private int price;

    private int salePrice;

    private int count;

    private MultipartFile multipartFile;

    public Product toEntity(){
        return Product.builder()
                .category(category)
                .name(name)
                .content(content)
                .price(price)
                .salePrice(salePrice)
                .count(count)
                .build();
    }

}


