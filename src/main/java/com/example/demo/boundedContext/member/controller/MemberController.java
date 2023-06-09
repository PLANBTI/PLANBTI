package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.redis.MemberDtoRepository;
import com.example.demo.boundedContext.member.dto.MemberChangeDto;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ShoppingBasketService shoppingBasketService;
    private final ApplicationEventPublisher publisher;
    private final Rq rq;

    @GetMapping("/mypage")
    public String showMyPage(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/myPage";
    }

    // 회원 정보 조회
    @GetMapping("/profile")
    public String showProfile(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/profile";
    }

    @GetMapping("/modify")
    public String modify(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("member", member);
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modify(@Valid MemberModifyDto dto) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        if (dto.isSame(member)) rq.historyBack("수정된 내용이 없습니다.");

        Member modify = memberService.modify(member, dto);

        publisher.publishEvent(new MemberChangeDto(modify.getId(),modify.getUsername(),modify.getEmail()));


        return "redirect:/member/profile";
    }

    @GetMapping("/shoppingbasket")
    public String showShoppingBasket(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        ShoppingBasket shoppingBasket = shoppingBasketService.findByMember(member.getId());
        List<Product> products = shoppingBasket.getProducts();
        model.addAttribute("shoppingId",shoppingBasket.getId());
        model.addAttribute("products", products);

        return "member/shoppingbasket";
    }

    @GetMapping("/testresult")
    public String showTestResult(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        model.addAttribute("testresults", member.getTests());
        return "member/testResult";
    }

    @GetMapping("/orderlist")
    public String showOrderlist(Model model) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        List<Order> orderList = orderService.findAllByMember(member);
        model.addAttribute("orderList", orderList);
        return "member/orderlist";
    }

    // soft-delete, 회원 탈퇴
    @GetMapping("/delete")
    public String delete() {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        memberService.delete(member);
        return "redirect:/logout";
    }

}
