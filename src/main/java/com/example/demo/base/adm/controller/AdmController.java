package com.example.demo.base.adm.controller;

import com.example.demo.base.adm.service.AdmOrderDetailService;
import com.example.demo.base.image.service.ImageService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.product.dto.ProductRegisterDto;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.service.ProductService;
import com.example.demo.boundedContext.product.service.ReviewService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adm")
public class AdmController {

    private final MemberService memberService;
    private final FaqService faqService;
    private final ReviewService reviewService;
    private final AdmOrderDetailService admOrderDetailService;
    private final Rq rq;
    private final ProductService productService;
    private final ImageService imageService;

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
        List<OrderDetail> orderDetails = admOrderDetailService.getPendingStatus();
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
        List<OrderDetail> orderDetails = admOrderDetailService.findByStatusIsNotPending();
        model.addAttribute("orderDetails", orderDetails);
        return "adm/deliveries";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceForm {
        @NotBlank
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
        List<OrderDetail> inProgressList = admOrderDetailService.getStatusInProgress();
        List<OrderDetail> completedList = admOrderDetailService.getCompletedStatus();
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

        orderDetail.updateStatus(APPROVED);
        return rq.redirectWithMsg("/adm/orders", "승인되었습니다.");
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
                .filter(od -> od.getOrder().getStatus().equals(OrderStatus.COMPLETE)).toList();

        if (!category.equals("all")) {
            orderDetails = orderDetails.stream()
                    .filter(od -> od.getProduct().getCategory().getName().equals(category))
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
        return "redirect:/adm/productList";
    }

    @GetMapping("/productRegister")
    public String RegisterProduct() {
        return "adm/productRegister";
    }

    @PostMapping("/registerpro")
    public String RegisterProductPro(ProductRegisterDto productRegisterDto, String url) {
        url = imageService.upload(productRegisterDto.getMultipartFile(), UUID.randomUUID().toString());
        productService.register(productRegisterDto, url);

        return "redirect:/adm/productList";
    }

}