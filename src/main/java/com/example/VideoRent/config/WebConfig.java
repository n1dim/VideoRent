package com.example.VideoRent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns(
                        "/admin/**",
                        "/movies/add",
                        "/movies/delete/**",
                        "/movies/edit/**",
                        "/movie/*/copies/add",
                        "/movie/*/copies/edit/**",
                        "/movie/*/issue",
                        "/media/add"
                );
    }
}
