// This package contains configuration classes for the application
package com.eazybytes.config;

// Import necessary Spring Security and configuration annotations
import com.eazybytes.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.eazybytes.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

// Import a static method for customizing security configurations with default settings
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

// Configuration class for security settings in production environment
@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    // Defines the primary security filter chain
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        // Configures CORS to allow cross-origin requests from Angular frontend
        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {

                    // Defines CORS rules to handle cross-origin requests
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                // Configures CSRF protection
                .csrf(csrfConfig ->
                        csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                                .ignoringRequestMatchers("/register", "/contact")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        // Enforces HTTPS for all requests
        http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure());

        // Restricts access to authenticated users for key endpoints
        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//                .requestMatchers( "/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
//                .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//                .requestMatchers( "/myCards").hasAuthority("VEIWCARDS")
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers( "/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myLoans").hasRole("USER")
                        .requestMatchers( "/myCards").hasRole("USER")
                .requestMatchers( "/user").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll()
        );
        // Enables form login with default settings
        http.formLogin(withDefaults());

        // Enables HTTP Basic authentication with custom entry point
        http.httpBasic(
                hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
        );

        // Custom exception handling for security access
        http.exceptionHandling(
                ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        // Builds and returns the configured SecurityFilterChain
        return http.build();
    }

    // Password encoder for secure password hashing and verification
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
