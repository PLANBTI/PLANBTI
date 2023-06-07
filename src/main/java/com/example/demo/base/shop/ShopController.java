package com.example.demo.base.shop;
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
import com.example.demo.boundedContext.category.service.CategoryService;
import com.example.demo.boundedContext.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.demo.boundedContext.category.entity.QCategory.category;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Controller
public class ShopController {

    private final ShopService shopService;
    private final CategoryService categoryService;

    @GetMapping("/shop")
    public String list(@PageableDefault(size = 12, page = 0, direction = DESC, sort = "created") Pageable pageable,
                       @RequestParam(value = "category", defaultValue = "istj") String category,
                       Model model) {
        Page<Product> paging = shopService.getList(category, pageable);

        List<Category> categories = categoryService.findAll();

        model.addAttribute("paging", paging);
        model.addAttribute("categories", categories);
        return "shop/shopMain";
    }
}




