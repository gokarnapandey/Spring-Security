// Package declaration
package com.eazybytes.exceptionhandling;

// Import necessary classes and interfaces
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

// Custom implementation of the AuthenticationEntryPoint interface
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Override the commence method to handle authentication exceptions
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Get the current timestamp
        LocalDateTime currentTimeStamp = LocalDateTime.now();

        // Get the error message from the authentication exception, or a default message if none is available
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";

        // Get the request path
        String path = request.getRequestURI();

        // Set a custom error reason header
        response.setHeader("eazybank-error-reason", "Authentication Failed");

        // Set the response status to UNAUTHORIZED (401)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Set the response content type to JSON
        response.setContentType("application/json;charset=UTF-8");

        // Create a JSON error response
        String jsonResponse =
                String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                        currentTimeStamp, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        message, path);

        // Write the JSON error response to the response writer
        response.getWriter().write(jsonResponse);
    }
}