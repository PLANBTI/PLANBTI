package com.example.demo.boundedContext.faq.dto;

import com.example.demo.boundedContext.faq.entity.Faq;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqModifyDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Email
    private String email;

    public boolean isSame(Faq faq) {
        return title.equals(faq.getTitle()) && content.equals(faq.getContent()) && email.equals(faq.getEmail());
    }
}
