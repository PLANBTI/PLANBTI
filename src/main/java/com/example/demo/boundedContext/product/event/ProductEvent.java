package com.example.demo.boundedContext.product.event;

import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class ProductEvent {

    private final ProductService productService;


    @Transactional
    @EventListener
    public void decreaseEvent(ProductDecreaseEvent event) {
        Long productId = event.getProductId();
        int count = event.getCount();

        Product product = productService.findById(productId);
        product.updateProductCount(count);
    }
}
