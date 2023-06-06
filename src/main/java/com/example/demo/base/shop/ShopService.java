package com.example.demo.base.shop;

import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopService {
    private final ProductRepository productRepository;

    public Page<Product> getList(int page) {
        Pageable pageable = PageRequest.of(page, 12);
        return this.productRepository.findAll(pageable);
    }

    /*
    public void create(String name, String price) {
        Product p = new Product();
        p.getName(name);
        p.getPrice(price);
        this.productRepository.save(p);
    } a
     */
}
