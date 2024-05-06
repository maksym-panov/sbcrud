package com.maksympanov.hneu.mjt.sbcrud.sbcrud.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }

}