package com.example.demo.boundedContext.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReviewDto {

    private String username;
    private String title;

    private String content;

    private String image;
    private int rate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDate;
}
