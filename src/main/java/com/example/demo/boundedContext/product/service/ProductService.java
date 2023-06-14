package com.example.demo.boundedContext.product.service;

import com.example.demo.base.exception.handler.DataNotFoundException;
import com.example.demo.base.image.service.ImageService;
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.product.dto.ProductDto;
import com.example.demo.boundedContext.product.dto.ProductRegisterDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("존재하지 않는 Product입니다."));
    }

    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 데이터입니다"));
    }

    public Page<Product> findAllByCategoryNameAndKeyword(String categoryName, String keyword, Pageable pageable) {
        return productRepository.findAllByCategoryNameAndKeyword(categoryName, keyword, pageable);
    }


    public List<Product> findAllById(List<Long> selectedProducts) {
        return productRepository.findAllById(selectedProducts);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    //soft delete
    public void delete(Product product) {
        Product deletedProduct = product.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        productRepository.save(deletedProduct);
    }
    public void register(ProductRegisterDto productRegisterDto, String url){
        Category category = categoryRepository.findByName(productRegisterDto.getCategory());

        Product product = productRegisterDto.toEntity(category);
        product.setUrl(url);
        productRepository.save(product);
    }

    
    public void modify(ProductRegisterDto productRegisterDto,String url){
        Category category = categoryRepository.findById(Long.parseLong(productRegisterDto.getCategory())).get();
        Long id=productRegisterDto.getId();
        Product product= productRepository.findById(id).get();
        product.setUrl(url);
        product.setName(productRegisterDto.getName());
        product.setContent(productRegisterDto.getContent());
        product.setCategory(category);
        product.setCount(productRegisterDto.getCount());
        product.setPrice(productRegisterDto.getPrice());
        product.setSalePrice(productRegisterDto.getSalePrice());
        productRepository.save(product);
    }


}
