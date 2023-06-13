package com.example.demo.boundedContext.product.entity;

import com.example.demo.boundedContext.product.dto.ProductDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "basket")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Basket {

    @Id
    private Long memberId;

    private List<ProductDto> products = new ArrayList<>();
    private int count = 0;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long time = 30L;

    public Basket(Long memberId) {
        this.memberId = memberId;
    }

    public void addProduct(Product product, int count) {

        Optional<ProductDto> optionalProductDto = findProduct(product.getId());

        time = 30L;

        if (optionalProductDto.isEmpty()) {
            this.products.add(new ProductDto(product, count));
            this.count += 1;
            return;
        }

        ProductDto productDto = optionalProductDto.get();
        productDto.addCount(count);
    }

    public Optional<ProductDto> findProduct(Long productId) {
        return products.stream().filter(i -> i != null && i.getId().equals(productId)).findFirst();
    }

    public int getProductCount(Long productId) {
        Optional<ProductDto> optionalProductDto = findProduct(productId);
        return optionalProductDto.isEmpty() ? 0 : optionalProductDto.orElseThrow().getCount();
    }

    public boolean deleteProduct(Long productId) {
        Optional<ProductDto> first = products.stream().filter(i -> i.getId().equals(productId)).findFirst();
        if (first.isEmpty())
            return false;

        ProductDto productDto = first.get();
        this.count -= 1;
        this.products.remove(productDto);
        return true;
    }
}
