package com.example.demo.base.shop;
import com.example.demo.boundedContext.product.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ShopController {
    private  ShopService shopService;

    @GetMapping("/shop")
    @ResponseBody
    public String shopMain() {

        return "shopMain";
    }


}




