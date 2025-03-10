package com.eazybytes.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Purpose: Custom filter to retrieve and utilize the CSRF token from the request attribute.
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    // Purpose: Filters incoming HTTP requests to extract the CSRF token.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Purpose: Extracts the CSRF token from the request attribute.
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Purpose: Retrieves the CSRF token value.
        csrfToken.getToken();

        // Purpose: Continues the filter chain to pass the request to the next filter.
        filterChain.doFilter(request, response);
    }
}
