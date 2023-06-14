package com.example.demo.base.adm.controller;

import com.example.demo.base.adm.service.AdmOrderDetailService;
import com.example.demo.base.image.service.ImageService;
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.service.CategoryService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.product.dto.ProductRegisterDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import com.example.demo.util.rq.Rq;
import com.example.demo.util.ut.Ut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
>>>>>>> e694d30 (feat : About, Main 페이지)
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;
import static com.example.demo.boundedContext.order.entity.OrderStatus.COMPLETE;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adm")
<<<<<<< HEAD
@Slf4j
=======
>>>>>>> e694d30 (feat : About, Main 페이지)
public class AdmController {

    private final MemberService memberService;
    private final FaqService faqService;
    private final ReviewService reviewService;
    private final AdmOrderDetailService admOrderDetailService;
    private final Rq rq;
    private final ProductService productService;
    private final ImageService imageService;
    private final CategoryService categoryService;


<<<<<<< HEAD
=======

>>>>>>> e694d30 (feat : About, Main 페이지)
    @GetMapping("")
    public String showAdmMain() {
        return "adm/main";
    }

    @GetMapping("/")
    public String showAdmMain2() {
        return "redirect:/adm";
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
        return rq.redirectWithMsg("/adm/members", "%d번 회원을 삭제하였습니다.".formatted(id));
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

    @GetMapping("/pay")
    public String showPay(Model model) {
        List<OrderDetail> orderDetails = admOrderDetailService.getStatusIsPending();
        model.addAttribute("orderDetails", orderDetails);
        return "adm/pay";
    }

    @GetMapping("/placed/{id}")
    public String updateToPlaced(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);
        admOrderDetailService.updateStatus(orderDetail, PLACED);
        return rq.redirectWithMsg("/adm/deliveries", "주문을 확인하였습니다.");
    }

    @GetMapping("/deliveries")
    public String showDeliveries(Model model) {
        List<OrderDetail> orderDetails = admOrderDetailService.getStatusIsNotPending();
        model.addAttribute("orderDetails", orderDetails);
        return "adm/deliveries";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceForm {
        @NotBlank
        @Size(min = 6, max = 16)
        private String invoiceNumber;
    }

    @PostMapping("/startDelivery/{id}")
    public String startDelivery(@PathVariable Long id, @Valid InvoiceForm form) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);
        admOrderDetailService.startDelivery(orderDetail, form.getInvoiceNumber());
        return rq.redirectWithMsg("/adm/deliveries", "운송장 번호가 저장되었습니다.");
    }

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<OrderDetail> inProgressList = admOrderDetailService.getStatusIsInProgress();
        List<OrderDetail> completedList = admOrderDetailService.getStatusIsCompleted();
        List<OrderDetail> allList = admOrderDetailService.findAll();

        model.addAttribute("inProgressList", inProgressList);
        model.addAttribute("completedList", completedList);
        model.addAttribute("allList", allList);
        return "adm/orders";
    }

    @GetMapping("/approve/{id}")
    public String approveExchange(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);

        if (!orderDetail.isEqualStatusTo(EXCHANGE)) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        admOrderDetailService.updateStatus(orderDetail, APPROVED);
        return rq.redirectWithMsg("/adm/orders", "교환 요청이 승인되었습니다.");
    }

    @GetMapping("/complete/{id}")
    public String isCompleted(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);

        if (!(orderDetail.isEqualStatusTo(APPROVED) || orderDetail.isEqualStatusTo(RETURN))) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        admOrderDetailService.updateStatus(orderDetail, COMPLETED);
        return rq.redirectWithMsg("/adm/orders", "교환(반품)이 완료되었습니다.");
    }

    @GetMapping("/sales")
    public String showSales(Model model,
                            @RequestParam(defaultValue = "2023") int year,
                            @RequestParam(defaultValue = "1") int month,
                            @RequestParam(defaultValue = "all") String category) {
        List<OrderDetail> orderDetails = admOrderDetailService.getMonthlyCompleted(year, month);

        orderDetails = orderDetails.stream()
                .filter(od -> od.getOrder().getStatus().equals(COMPLETE)).toList();

        if (!category.equals("all")) {
            orderDetails = orderDetails.stream()
                    .filter(od -> od.getProduct().isEqualCategoryTo(category))
                    .toList();
        }

        Long totalSales = orderDetails.stream().mapToLong(OrderDetail::getAmount).sum();

        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("totalSales", totalSales);
        return "adm/sales";
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
<<<<<<< HEAD
        return rq.redirectWithMsg("/adm/productList", "상품이 목록에서 삭제되었습니다.");
=======
        return  rq.redirectWithMsg("/adm/productList", "상품이 목록에서 삭제되었습니다.");
>>>>>>> e694d30 (feat : About, Main 페이지)
    }

    @GetMapping("/productRegister")
    public String RegisterProduct(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "adm/productRegister";
    }

<<<<<<< HEAD

    private boolean isProductRegisterDtoValid(ProductRegisterDto productRegisterDto) {
        return productRegisterDto != null &&
                productRegisterDto.getFile() != null &&
                productRegisterDto.getFile().getSize() > 0 &&
                productRegisterDto.getContent() != null &&
                !productRegisterDto.getContent().isBlank() &&
                productRegisterDto.getName() != null &&
                !productRegisterDto.getName().isBlank();

    }


    @PostMapping("/registerpro")
    public String RegisterProductPro(ProductRegisterDto productRegisterDto) {
        System.out.println("productRegisterDto : " + productRegisterDto);

        if (!isProductRegisterDtoValid(productRegisterDto)) {
            return rq.historyBack("모든 항목을 입력하세요.");
        }


        String url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        productService.register(productRegisterDto, url);

        return rq.redirectWithMsg("/adm/productList", "상품이 등록되었습니다.");
    }


    @GetMapping("/modifyProduct/{id}")
    public String modifyProduct(@PathVariable Long id, Model model) {
        Product product = this.productService.findById(id);
=======
    @PostMapping("/registerpro")
    public String RegisterProductPro(ProductRegisterDto productRegisterDto){
        String url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        productService.register(productRegisterDto, url);

        return "redirect:/adm/productList";
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyProduct/{id}")
    public String modifyProduct(@PathVariable Long id,Model model){
        Product product=this.productService.findById(id);
>>>>>>> e694d30 (feat : About, Main 페이지)
        model.addAttribute("product", product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "adm/productModify";
    }

    @PostMapping("/modifypro")
<<<<<<< HEAD
    public String ModifyProductPro(ProductRegisterDto productRegisterDto, String url) {
        if (!productRegisterDto.getFile().isEmpty()) {
            url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        }
        productService.modify(productRegisterDto, url);
=======
    public String ModifyProductPro(ProductRegisterDto productRegisterDto,String url){
        if (!productRegisterDto.getFile().isEmpty()){
            url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        }
        productService.modify(productRegisterDto,url);
>>>>>>> e694d30 (feat : About, Main 페이지)
        return "redirect:/adm/productList";
    }


}

