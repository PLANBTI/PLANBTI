package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.dto.MemberChangeDto;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import com.example.demo.boundedContext.member.entity.MbtiTest;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.boundedContext.member.service.TestService;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.boundedContext.product.entity.Basket;
import com.example.demo.boundedContext.product.repository.BasketRepository;
import com.example.demo.util.rq.Rq;
import com.nimbusds.oauth2.sdk.util.StringUtils;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private  final TestService testService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final ApplicationEventPublisher publisher;
    private final BasketRepository basketRepository;
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

        Optional<Basket> basketOptional = basketRepository.findById(member.getId());
        Basket basket = new Basket(member.getId());
        if (basketOptional.isPresent()) {
            basket = basketOptional.get();
        }
        model.addAttribute("products",basket);

        return "member/shoppingbasket";
    }

    @GetMapping("/testresult")
    public String showTestResult(Model model, HttpServletRequest request) {
        // 쿠키 값들을 보관할 Map 생성
        Map<String, String> cookieValues = new HashMap<>();

        // 쿠키 값들을 Map에 추출
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookieValues.put(cookie.getName(), cookie.getValue());
            }
        }
        // 이제 필요한 값들을 다음과 같이 가져올 수 있습니다
        String mbti = cookieValues.get("mbti");
        String plantName = cookieValues.get("plantName");
        String plantDescription = cookieValues.get("plantDescription");

        // 로그인된 사용자 정보를 가져옵니다
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        if(!testService.isMemberExist(member.getUsername()) && (StringUtils.isBlank(mbti) || StringUtils.isBlank(plantName) || StringUtils.isBlank(plantDescription))){
            return rq.historyBack("테스트를 진행해 주세요");
        }
        // 모든 필요한 값들이 존재하는지 확인
        else if (!StringUtils.isBlank(mbti) && !StringUtils.isBlank(plantName) && !StringUtils.isBlank(plantDescription)) {
            testService.createTestResult(member, mbti, plantName, plantDescription);
        }

        List<MbtiTest> tests = testService.findAllTestsByMember(member);
        model.addAttribute("tests", tests);
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
