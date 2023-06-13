package com.example.demo.boundedContext.order.controller;

import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.dto.OrderRequestDto;
import com.example.demo.boundedContext.order.service.OrderDetailService;
import com.example.demo.boundedContext.order.service.OrderService;
import com.example.demo.util.rq.ResponseData;
import com.example.demo.util.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.boundedContext.order.entity.OrderStatus.BEFORE;
import static com.example.demo.boundedContext.order.entity.OrderStatus.COMPLETE;


@Tag(name = "Order Controller",description = "주문, 결과, 교환 ,반품 controller")
@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final Rq rq;

    @Operation(summary = "결제 페이지 ",description = "주문 시 해당 주문 목록을 보여주며 결제 또한 가능합니다.")
    @GetMapping("/orderPage")
    public String orderPage(Model model) {
        ResponseData<OrderRequestDto> responseData = orderService.findLastOrderByStatus(rq.getMemberId(), BEFORE);
        model.addAttribute("order", responseData.getContent());
        model.addAttribute("username",rq.getUsername());
        
        return "order/orderPage";

    }

    @Operation(summary = "결제 완료 페이지 ",description = "결제가 정상적으로 완료되면 결과 페이지를 보여줍니다.")
    @GetMapping("/result")
    public String resultPage(Model model) {
        ResponseData<OrderRequestDto> lastOrderComplete = orderService.findLastOrderByStatus(rq.getMemberId(),COMPLETE);
        model.addAttribute("order", lastOrderComplete.getContent());
        return "order/result";
    }

    @Operation(summary = "주문 상태 확인 페이지 ",description = "현재 주문 상태를 조회하여 보여줍니다.")
    @GetMapping("/orderInfo/{id}")
    public String orderStatus(Model model,@PathVariable("id") Long orderId) {

        ResponseData<OrderRequestDto> lastOrderComplete =
                orderService.findOrderByOrderId(rq.getMemberId(),orderId);

        model.addAttribute("order", lastOrderComplete.getContent());
        return "order/orderInfo";
    }

    @Operation(summary = "교환 요청 가능 물품", description = "교환 가능한 물픔을 보여줍니다.")
    @GetMapping("/exchange/{id}")
    public String exchangeGet(Model model,
                              @PathVariable(name = "id") Long orderId,
                              @RequestParam Long orderItemId) {

        OrderExchangeDto dto = orderDetailService.findOrderDetailById(orderId, orderItemId, rq.getMemberId());

        model.addAttribute("orderDetail", dto);
        return "order/exchange";
    }

    @Operation(summary = "교환 신청", description = "선택한 상품에 대한 교환을 신청합니다.")
    @Parameters({
            @Parameter(name = "OrderExchangeDto",description = "교환 하고 싶은 물품의 아이디와 개수"),
            @Parameter(name = "orderId",description = "주문 번호")
    })

    @PostMapping("/exchange/{id}")
    public String exchangePost(
            @PathVariable(name = "id") Long orderId,
            @Valid OrderExchangeDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return rq.historyBack("실패하였습니다. 잠시후에 다시 이용해주세요");
        }

        if (!dto.isReturn()) {
            orderDetailService.exchange(dto,rq.getMemberId());
        } else {
            orderDetailService.returnProduct(orderId, dto, rq.getMemberId());
        }

        return rq.redirectWithMsg("/order/orderInfo/%d".formatted(orderId),"교환(반품)이 신청되었습니다.");
    }
}
