package com.example.demo.base.shop;
import com.example.demo.boundedContext.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/shop")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Product> paging = shopService.getList(page);
        model.addAttribute("paging", paging);
        return "shop/shopMain";
    }


}




