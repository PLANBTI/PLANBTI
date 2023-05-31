package com.example.demo.base.shop;

import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopService {
    private final ProductRepository productRepository;

    public List<Product> getList() {
        return this.productRepository.findAll();
    }
}
