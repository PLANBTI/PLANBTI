package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.AddressService;
import com.example.demo.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/address")
@PreAuthorize("isAuthenticated()")
public class AddressController {

    private final AddressService addressService;
    private final MemberService memberService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressForm {
        private String name;
        private String addr;
        private String addrDetail;
        @Size(min = 5, max = 7)
        private String zipCode;
        private String phoneNumber;
    }

    @GetMapping("/create")
    public String create() {
        return "address/create";
    }

    @PostMapping("/create")
    public String create(@Valid AddressForm form, @RequestParam(name = "isDefault", required = false) boolean isDefault,
                         @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        addressService.create(member, form.getName(), form.getAddr(), form.getAddrDetail(), form.getZipCode(), form.getPhoneNumber(), isDefault);
        return "redirect:/member/profile";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);

        if (address == null) {
            throw new DataNotFoundException("address not found");
        }

        model.addAttribute("address", address);
        return "address/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid AddressForm form, @RequestParam(name = "isDefault", required = false) boolean isDefault,
                         @AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());
        addressService.modify(member, id, form.getName(), form.getAddr(), form.getAddrDetail(), form.getZipCode(), form.getPhoneNumber(), isDefault);

        return "redirect:/member/profile";
    }

    // soft-delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CustomOAuth2User user) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        Member member = memberService.findByUsernameAndDeleteDateIsNull(user.getName());

        addressService.delete(member, address);
        return "redirect:/member/profile";
    }
}
