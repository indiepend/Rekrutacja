package me.dworak.rekrutacja.web;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
public class CustomWebConfigurer implements WebMvcConfigurer {

    private final CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver;

    public CustomWebConfigurer(CustomWebHandlerExceptionResolver customWebHandlerExceptionResolver) {
        this.customWebHandlerExceptionResolver = customWebHandlerExceptionResolver;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(customWebHandlerExceptionResolver);
    }
}
