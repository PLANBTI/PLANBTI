package com.example.demo.base.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "MBTI PLANT SHOP",
        description = "chatGpt를 활용한 MBTI 기반 식물 추천 서비스",
        version = "v1.0.0"))
@Configuration
public class SwaggerConfig {

}
