package me.dworak.rekrutacja.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dworak.rekrutacja.api.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

public class CustomWebHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(CustomWebHandlerExceptionResolver.class);
    private final ObjectMapper objectMapper;

    public CustomWebHandlerExceptionResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = null;

        try {
            if (ex instanceof UserNotFoundException exception) {
                modelAndView = handleUserNotFound(exception, request, response, handler);
            } else if (ex instanceof HttpMediaTypeNotAcceptableException exception) {
                modelAndView = handleHttpMediaTypeNotAcceptable(exception, request, response, handler);
            }
            return modelAndView != null ? modelAndView : new ModelAndView();
        } catch(IOException e){
            log.debug("Exception was thrown", e);
        }
        return null;
    }

    @Override
    protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            @Nullable Object handler) throws IOException {

        response.getOutputStream()
                .write(
                        objectMapper.writeValueAsBytes(
                                ErrorDto.errorDto(
                                        ex.getMessage(),
                                        ex.getStatusCode().toString())));
        response.setStatus(ex.getStatusCode().value());
        return null;
    }

    protected ModelAndView handleUserNotFound(UserNotFoundException ex,
                                              HttpServletRequest request,
                                              HttpServletResponse response,
                                              @Nullable Object handler) throws IOException {

        response.getOutputStream()
                .write(
                        objectMapper.writeValueAsBytes(
                                ErrorDto.errorDto(
                                        ex.getMessage(),
                                        HttpStatus.NOT_FOUND.toString())));
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }
}
