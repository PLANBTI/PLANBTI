package com.example.demo.boundedContext.faq.dto;

import com.example.demo.boundedContext.faq.entity.FaqCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.demo.boundedContext.faq.entity.FaqCategory.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqDto {

    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    private String category;

    @NotBlank
    private String content;

    @Email
    private String email;

    public FaqCategory getCategory() {
        FaqCategory category;
        category = switch (this.category) {
            case "PRODUCT" -> PRODUCT;
            case "SHIPPING" -> SHIPPING;
            case "EXCHANGE" -> EXCHANGE;
            case "RETURN" -> RETURN;
            default -> ETC;
        };
        return category;
    }
}
