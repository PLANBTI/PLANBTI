package com.example.demo.boundedContext.member.controller;

import com.example.demo.boundedContext.member.dto.AddressDto;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.AddressService;
import com.example.demo.boundedContext.member.service.MemberService;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Tag(name = "주소 컨트롤러")
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

    @Operation(summary = "주소 생성",description = "회원의 기본 배송지를 생성합니다.")
    @Parameters({
            @Parameter(name = "dto",description = "주소 상세 정보"),
            @Parameter(name = "isDefault",description = "기본 주소 확인"),
    })
    @PostMapping("/create")
    public String create(@Valid AddressDto dto, @RequestParam(name = "isDefault", required = false) boolean isDefault) {
        Member member = memberService.findByUsername(rq.getUsername());
        Address defaultAddress = getDefaultAddress(member);

        if(defaultAddress != null && isDefault) {
            addressService.modifyDefault(defaultAddress);
            addressService.create(member, dto, isDefault);
            return rq.redirectWithMsg("/member/profile", "기본 배송지를 변경하였습니다.");
        }

        addressService.create(member, dto, isDefault);
        return rq.redirectWithMsg("/member/profile", "배송지를 추가하였습니다.");
    }

    @Operation(summary = "주소 정보 수정 GET")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        model.addAttribute("address", address);
        return "address/modify";
    }

    @Operation(summary = "주소 정보 수정 요청",description = "회원의 배송지 주소를 수정합니다.")
    @Parameters({
            @Parameter(name = "dto",description = "주소 상세 정보"),
            @Parameter(name = "isDefault",description = "기본 주소 확인"),
    })
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid AddressDto dto,
                         @RequestParam(name = "isDefault", required = false) boolean isDefault) {
        Member member = memberService.findByUsername(rq.getUsername());
        Address address = addressService.findByIdAndDeleteDateIsNull(id);

        if(!address.getMember().getUsername().equals(member.getUsername())) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        if(dto.isSame(address)) rq.historyBack("수정된 내용이 없습니다.");

        Address defaultAddress = getDefaultAddress(member);

        if(defaultAddress != null && isDefault && defaultAddress.getId() != address.getId()) {
            addressService.modifyDefault(defaultAddress);
            addressService.modify(member, address, dto, isDefault);
            return rq.redirectWithMsg("/member/profile", "기본 배송지를 변경하였습니다.");
        }

        addressService.modify(member, address, dto, isDefault);
        return rq.redirectWithMsg("/member/profile", "배송지를 변경하였습니다.");
    }

    private Address getDefaultAddress(Member member) {
        return member.getAddresses().stream().filter(Address::isDefault).findFirst().orElse(null);
    }

    @Operation(summary = "주소 정보 삭제",description = "회원의 배송지 주소를 삭제합니다.")
    // soft-delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Address address = addressService.findByIdAndDeleteDateIsNull(id);
        Member member = memberService.findByUsername(rq.getUsername());

        if(!address.getMember().equals(member)) {
            return rq.historyBack("접근 권한이 없습니다.");
        }

        addressService.delete(member, address);
        return rq.redirectWithMsg("/member/profile", "배송지를 삭제하였습니다.");
    }
}