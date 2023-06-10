package com.example.demo.boundedContext.order.entity;

public enum OrderItemStatus {
    PENDING("결제 완료"),
    PLACED("배송 준비 중"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("취소"),
    EXCHANGE("교환 요청"),
    RETURN("반품"),
    APPROVED("승인 완료"),
    COMPLETED("구매 확정");

    private String status;

    OrderItemStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return status;
    }
}
