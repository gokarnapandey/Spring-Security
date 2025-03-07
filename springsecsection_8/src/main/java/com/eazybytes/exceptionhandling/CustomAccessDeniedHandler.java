package com.eazybytes.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Get the current timestamp
        LocalDateTime currentTimeStamp = LocalDateTime.now();

        // Get the error message from the access denied exception, or a default message if none is available
        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Access Denied";

        // Get the request path
        String path = request.getRequestURI();

        // Set a custom error reason header
        response.setHeader("eazybank-denied-reason", "Authorization Failed");

        // Set the response status to FORBIDDEN (403)
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Set the response content type to JSON
        response.setContentType("application/json;charset=UTF-8");

        // Create a JSON error response
        String jsonResponse =
                String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                        currentTimeStamp, HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                        message, path);

        // Write the JSON error response to the response writer
        response.getWriter().write(jsonResponse);
    }
}
