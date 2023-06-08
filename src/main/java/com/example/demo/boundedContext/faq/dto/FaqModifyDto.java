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

    @Override
    public boolean equals(Object o) {
        Faq faq = (Faq) o;
        return Objects.equals(title, faq.getTitle()) && Objects.equals(content, faq.getContent()) && Objects.equals(email, faq.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, email);
    }
}
