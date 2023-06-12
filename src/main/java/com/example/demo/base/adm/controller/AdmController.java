package com.example.demo.base.adm.controller;

import com.example.demo.base.image.service.ImageService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.product.dto.ProductDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adm")
public class AdmController {

    private final MemberService memberService;
    private final FaqService faqService;
    private final ReviewService reviewService;
    private final ProductService productService;
    
    private final ImageService imageService;

    private final ApplicationEventPublisher publisher;


    @GetMapping("")
    public String showAdmMain() {
        return "adm/main";
    }

    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "adm/members";
    }

    @GetMapping("/deleteMember/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteHard(id);
        return "redirect:/adm/members";
    }

    @GetMapping("/faq")
    public String showFaq(Model model) {
        List<Faq> faqList = faqService.findAll();
        model.addAttribute(faqList);
        return "adm/faqList";
    }

    @GetMapping("/reviews")
    public String showReviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "adm/reviews";
    }

    @GetMapping("/productList")
    public String showProduct(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "adm/productList";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        productService.delete(product);
        return "redirect:/adm/productList";
    }

    @GetMapping("/productRegister")
    public String RegisterProduct() {
        return "adm/productRegister";
    }

    @PostMapping("/registerpro")
    public String RegisterProductPro(ProductDto productDto,String url){
        url = imageService.upload(productDto.getMultipartFile(), UUID.randomUUID().toString());
        productService.register(productDto,url);

        return "redirect:/adm/productList";
    }


}
