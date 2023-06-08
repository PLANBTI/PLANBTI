package com.example.demo.base.shop;

import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import com.example.demo.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopService {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> getList(String categoryName, Pageable pageable) {
        return productService.findAllByCategoryName(categoryName, pageable);
    }

    public Optional<Category> categoryView(Long id){
        return categoryRepository.findById(id);
    }

//    public  Page<Product> productSearchList(String searchKeyword,Pageable pageable){
//        return productRepository.findByTitleContaining(searchKeyword, pageable);
//    }



}
