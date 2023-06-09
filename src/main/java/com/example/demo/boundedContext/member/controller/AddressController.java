package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.dto.AddressDto;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.AddressService;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.util.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final Rq rq;

    @GetMapping("/create")
    public String create() {
        return "address/create";
    }

    @PostMapping("/create")
    public String create(@Valid AddressDto dto, @RequestParam(name = "isDefault", required = false) boolean isDefault) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        Address address = getDefaultAddress(member);

        if(address != null && isDefault) {
            addressService.modifyDefault(address);
            addressService.create(member, dto, isDefault);
            return rq.redirectWithMsg("/member/profile", "기본 배송지를 변경하였습니다.");
        }

        addressService.create(member, dto, isDefault);
        return rq.redirectWithMsg("/member/profile", "배송지를 추가하였습니다.");
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("address", address);
        return "address/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid AddressDto dto,
                         @RequestParam(name = "isDefault", required = false) boolean isDefault) {
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());
        Address address = addressService.findByIdAndDeleteDateIsNull(id);

        if(dto.isSame(address)) rq.historyBack("수정된 내용이 없습니다.");

        Address defaultAddress = getDefaultAddress(member);

        if(!defaultAddress.equals(address) && isDefault) {
            addressService.modifyDefault(defaultAddress);
            addressService.modify(member, address, dto, isDefault);
            return rq.redirectWithMsg("/member/profile", "기본 배송지를 변경하였습니다.");
        }

        addressService.modify(member, address, dto, isDefault);
        return rq.redirectWithMsg("/member/profile", "배송지를 변경하였습니다.");
    }

    private Address getDefaultAddress(Member member) {
        return member.getAddresses().stream().filter(address -> address.isDefault()).findFirst().orElse(null);
    }

    // soft-delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        Member member = memberService.findByUsernameAndDeleteDateIsNull(rq.getUsername());

        addressService.delete(member, address);
        return rq.redirectWithMsg("/member/profile", "배송지를 삭제하였습니다.");
    }
}
