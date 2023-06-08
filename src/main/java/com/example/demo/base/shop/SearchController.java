package com.example.demo.base.shop;

import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import com.example.demo.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/search")
    public String showSearch(@PageableDefault(size = 12, page = 0, direction = DESC, sort = "created") Pageable pageable,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             Model model) {

        Page<Product> paging = productService.findAllByCategoryNameAndKeyword(null, keyword, pageable);

        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "shop/search";
    }
}
