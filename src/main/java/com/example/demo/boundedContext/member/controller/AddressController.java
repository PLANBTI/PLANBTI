package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.base.security.CustomOAuth2User;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/address")
@PreAuthorize("isAuthenticated()")
public class AddressController {

    private final AddressService addressService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @NotBlank
    public static class AddressForm {
        private String name;
        private String addr;
        private String addrDetail;
        @Size(min = 5, max = 7)
        private String zipCode;
        private String phoneNumber;
        private boolean isDefault;
    }

    @GetMapping("/create")
    public String create() {
        return "address/create";
    }

    @PostMapping("/create")
    public String create(@Valid AddressForm form, @AuthenticationPrincipal CustomOAuth2User user) {
        addressService.create(form.getName(), form.getAddr(), form.getAddrDetail(), form.getZipCode(), form.getPhoneNumber(), form.isDefault());
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
    public String modify(@PathVariable Long id, @Valid AddressForm form) {
        addressService.modify(id, form.getName(), form.getAddr(), form.getAddrDetail(), form.getZipCode(), form.getPhoneNumber(), form.isDefault());

        return "redirect:/member/profile";
    }

    // soft-delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        addressService.delete(address);
        return "redirect:/member/profile";
    }
}
