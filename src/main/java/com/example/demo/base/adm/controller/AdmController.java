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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;
import static com.example.demo.boundedContext.order.entity.OrderStatus.COMPLETE;

@Tag(name = "관리자")
@RequiredArgsConstructor
@Controller
@RequestMapping("/adm")
@Slf4j
public class AdmController {

    private final MemberService memberService;
    private final FaqService faqService;
    private final ReviewService reviewService;
    private final AdmOrderDetailService admOrderDetailService;
    private final Rq rq;
    private final ProductService productService;
    private final ImageService imageService;
    private final CategoryService categoryService;

    @Operation(summary = "관리자 홈페이지")
    @GetMapping("")
    public String showAdmMain() {
        return "adm/main";
    }

    @Operation(summary = "관리자 홈페이지")
    @GetMapping("/")
    public String showAdmMain2() {
        return "redirect:/adm";
    }

    @Operation(summary = "관리자 회원 홈페이지")
    @GetMapping("/members")
    public String showMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "adm/members";
    }

    @Operation(summary = "회원 삭제",description = "회원을 삭제합니다.")
    @GetMapping("/deleteMember/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteHard(id);
        return rq.redirectWithMsg("/adm/members", "%d번 회원을 삭제하였습니다.".formatted(id));
    }

    @Operation(summary = "문의",description = "문의 리스트를 보여줍니다.")
    @GetMapping("/faq")
    public String showFaq(Model model) {
        List<Faq> faqList = faqService.findAll();
        model.addAttribute(faqList);
        return "adm/faqList";
    }

    @Operation(summary = "리뷰",description = "리뷰를 보여줍니다.")
    @GetMapping("/reviews")
    public String showReviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "adm/reviews";
    }

    @Operation(summary = "결제",description = "결제 리스트를 보여줍니다.")
    @GetMapping("/pay")
    public String showPay(Model model) {
        List<OrderDetail> orderDetails = admOrderDetailService.getStatusIsPending();
        model.addAttribute("orderDetails", orderDetails);
        return "adm/pay";
    }

    @Operation(summary = "주문 확인",description = "주문을 확인합니다")
    @GetMapping("/placed/{id}")
    public String updateToPlaced(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);
        admOrderDetailService.updateStatus(orderDetail, PLACED);
        return rq.redirectWithMsg("/adm/deliveries", "주문을 확인하였습니다.");
    }

    @Operation(summary = "배송 확인",description = "배송 상태를 확인합니다")
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

    @Operation(summary = "배송 시작 확인",description = "배송을 시작합니다.")
    @PostMapping("/startDelivery/{id}")
    public String startDelivery(@PathVariable Long id, @Valid InvoiceForm form) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);
        admOrderDetailService.startDelivery(orderDetail, form.getInvoiceNumber());
        return rq.redirectWithMsg("/adm/deliveries", "운송장 번호가 저장되었습니다.");
    }

    @Operation(summary = "주문 확인",description = "주문과 관련된 데이터들을 보여줍니다.")
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

    @Operation(summary = "교환 요청 확인",description = "교환 요청에 대하여 승인 처리합니다.")
    @GetMapping("/approve/{id}")
    public String approveExchange(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);

        if (!orderDetail.isEqualStatusTo(EXCHANGE)) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        admOrderDetailService.updateStatus(orderDetail, APPROVED);
        return rq.redirectWithMsg("/adm/orders", "교환 요청이 승인되었습니다.");
    }

    @Operation(summary = "교환(반품)",description = "교환(반품)과 관련된 데이터들을 보여줍니다.")
    @GetMapping("/complete/{id}")
    public String isCompleted(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderDetailService.findById(id);

        if (!(orderDetail.isEqualStatusTo(APPROVED) || orderDetail.isEqualStatusTo(RETURN))) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        admOrderDetailService.updateStatus(orderDetail, COMPLETED);
        return rq.redirectWithMsg("/adm/orders", "교환(반품)이 완료되었습니다.");
    }

    @Operation(summary = "매출",description = "매출현황을 보여줍니다.")
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

    @Operation(summary = "상품",description = "상품리스트를 보여줍니다.")
    @GetMapping("/productList")
    public String showProduct(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "adm/productList";
    }

    @Operation(summary = "상품 삭제",description = "상품을 삭제합니다.")
    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        productService.delete(product);
        return rq.redirectWithMsg("/adm/productList", "상품이 목록에서 삭제되었습니다.");
    }

    @Operation(summary = "상품 등록 페이지",description = "상품을 등록 페이지를 보여줍니다.")
    @GetMapping("/productRegister")
    public String RegisterProduct(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "adm/productRegister";
    }

    private boolean isProductRegisterDtoValid(ProductRegisterDto productRegisterDto) {
        return productRegisterDto != null &&
                productRegisterDto.getFile() != null &&
                productRegisterDto.getFile().getSize() > 0 &&
                productRegisterDto.getContent() != null &&
                !productRegisterDto.getContent().isBlank() &&
                productRegisterDto.getName() != null &&
                !productRegisterDto.getName().isBlank();

    }

    @Operation(summary = "상품 등록",description = "상품을 등록합니다.")
    @PostMapping("/registerpro")
    public String registerProductPro(ProductRegisterDto productRegisterDto) {
        if (!isProductRegisterDtoValid(productRegisterDto)) {
            return rq.historyBack("모든 항목을 입력하세요.");
        }

        String url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        productService.register(productRegisterDto, url);

        return rq.redirectWithMsg("/adm/productList", "상품이 등록되었습니다.");
    }

    @Operation(summary = "상품 수정 페이지",description = "상품을 수정 페이지를 보여줍니다.")
    @GetMapping("/modifyProduct/{id}")
    public String modifyProduct(@PathVariable Long id, Model model) {
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "adm/productModify";
    }

    @Operation(summary = "상품 수정",description = "상품을 수정합니다.")
    @PostMapping("/modifypro")
    public String modifyProductPro(ProductRegisterDto productRegisterDto, String url) {
        if (!productRegisterDto.getFile().isEmpty()) {
            url = imageService.upload(productRegisterDto.getFile(), UUID.randomUUID().toString());
        }
        productService.modify(productRegisterDto, url);
        return "redirect:/adm/productList";
    }

}