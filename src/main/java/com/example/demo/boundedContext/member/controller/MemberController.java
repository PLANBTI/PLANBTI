package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.redis.MemberDtoRepository;
import com.example.demo.boundedContext.member.dto.MemberChangeDto;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.member.service.TestService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.ShoppingBasket;
import com.example.demo.boundedContext.product.service.ShoppingBasketService;
import com.example.demo.util.rq.Rq;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private  final TestService testService;
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
    public String showTestResult(Model model, HttpServletRequest request) {
        String mbti = null;
        String plantName = null;  // 두 번째 쿠키의 이름을 적어주세요.
        String plantDescription = null;  // 세 번째 쿠키의 이름을 적어주세요.

        // 쿠키 읽기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("mbti")) {
                    mbti = cookie.getValue();
                } else if (cookie.getName().equals("plantName")) {
                    plantName = cookie.getValue();
                } else if (cookie.getName().equals("plantDescription")) {
                    plantDescription = cookie.getValue();
                }
            }
        }

        // 쿠키가 없거나 찾지 못한 경우 에러 처리
        if(mbti == null || plantName == null || plantDescription == null) {
            return "redirect:/error";
        }

        // 로그인된 사용자 정보를 가져옵니다
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        try {
            String decodedPlantName = URLDecoder.decode(plantName, "UTF-8");
            String decodedPlantDescription = URLDecoder.decode(plantDescription, "UTF-8");

                // 이제 decodedValue 변수에는 원래의 쿠키 값이 저장되어 있습니다.
                // 쿠키로부터 얻은 값을 가지고 새로운 MbtiTest를 생성하고 저장합니다
//                MbtiTest mbtiTest = MbtiTest.builder()
//                        .member(member)
//                        .title(decodedPlantName)
//                        .content(decodedPlantDescription)
//                        .build();
            if (!testService.isTestExist(mbti, decodedPlantName, decodedPlantDescription)){
                MbtiTest mbtiTest = testService.create(member, mbti, decodedPlantName, decodedPlantDescription);
                model.addAttribute("testresults", mbtiTest);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
