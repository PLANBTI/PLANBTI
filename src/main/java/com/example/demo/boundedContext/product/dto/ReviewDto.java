package com.example.demo.boundedContext.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReviewDto {

    private String username;
    private String title;

    private String content;

    private String image;
    private int rate;
    private LocalDateTime reviewDate;
}
