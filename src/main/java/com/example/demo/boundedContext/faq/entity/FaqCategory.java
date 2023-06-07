package com.example.demo.boundedContext.faq.entity;

public enum FaqCategory {
    PRODUCT("상품 관련 문의"),
    SHIPPING("배송 관련 문의"),
    EXCHANGE("교환 관련 문의"),
    RETURN("반품 관련 문의"),
    ETC("기타 문의");

    private String category;

    FaqCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
