package com.fizz.fizz_server.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fizz.fizz_server.global.base.response.ResponseUtil.createFailureResponse;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        String errorResponse = objectMapper.writeValueAsString(createFailureResponse(ExceptionType.FORBIDDEN));
        response.getWriter().write(errorResponse);
    }
}