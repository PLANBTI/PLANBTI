package com.example.demo.base.adm.controller;

import com.example.demo.base.adm.service.AdmOrderService;
import com.example.demo.boundedContext.faq.Service.FaqService;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.product.entity.Review;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adm")
public class AdmController {

    private final MemberService memberService;
    private final FaqService faqService;
    private final ReviewService reviewService;
    private final AdmOrderService admOrderService;
    private final Rq rq;

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
        List<OrderDetail> orderDetails = admOrderService.getPendingStatus();
        model.addAttribute("orderDetails", orderDetails);
        return "adm/pay";
    }

    @GetMapping("/placed/{id}")
    public String updateToPlaced(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderService.findById(id);
        admOrderService.updateStatus(orderDetail, PLACED);
        return rq.redirectWithMsg("/adm/deliveries", "주문을 확인하였습니다.");
    }

    @GetMapping("/deliveries")
    public String showDeliveries(Model model) {
        List<OrderDetail> orderDetails = admOrderService.findByStatusIsNotPending();
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
        OrderDetail orderDetail = admOrderService.findById(id);
        admOrderService.startDelivery(orderDetail, form.getInvoiceNumber());
        return rq.redirectWithMsg("/adm/deliveries", "운송장 번호가 저장되었습니다.");
    }

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<OrderDetail> inProgressList = admOrderService.getStatusInProgress();
        List<OrderDetail> completedList = admOrderService.getCompletedStatus();
        List<OrderDetail> allList = admOrderService.findAll();
        model.addAttribute("inProgressList", inProgressList);
        model.addAttribute("completedList", completedList);
        model.addAttribute("allList", allList);
        return "adm/orders";
    }

    @GetMapping("/approve/{id}")
    public String approveExchange(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderService.findById(id);

        if(!orderDetail.getStatus().equals(EXCHANGE)) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        orderDetail.updateStatus(APPROVED);
        return rq.redirectWithMsg("/adm/orders", "승인되었습니다.");
    }

    @GetMapping("/complete/{id}")
    public String isCompleted(@PathVariable Long id) {
        OrderDetail orderDetail = admOrderService.findById(id);

        if(!(orderDetail.getStatus().equals(APPROVED) || orderDetail.getStatus().equals(RETURN))) {
            return rq.historyBack("유효하지 않은 데이터입니다.");
        }

        admOrderService.updateStatus(orderDetail, COMPLETED);
        return rq.redirectWithMsg("/adm/orders", "교환(반품)이 완료되었습니다.");
    }

}