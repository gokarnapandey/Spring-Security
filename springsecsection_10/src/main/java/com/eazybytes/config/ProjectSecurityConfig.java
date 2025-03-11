package com.eazybytes.config;

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
import javax.sql.DataSource;
import java.util.Collections;
import static org.springframework.security.config.Customizer.withDefaults;

// Configuration class for Spring Security settings
@Configuration
@Profile("!prod") // This configuration will be active only when the "prod" profile is NOT active
public class ProjectSecurityConfig {

    /**
     * Configures the security filter chain, handling session management, CORS, CSRF, and request authorization.
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        // Security Context and Session Management Configuration
        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                // CORS Configuration to enable cross-origin requests from Angular frontend
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L); // Cache the pre-flight request for 1 hour
                        return config;
                    }
                }))

                // CSRF Configuration - Disables CSRF for '/register' and '/contact' endpoints
                .csrf(csrfConfig ->
                        csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                                .ignoringRequestMatchers("/register", "/contact")
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)

                // Channel Security - Enforce HTTP instead of HTTPS (not recommended for production)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())

                // Authorization Configuration - Restrict and allow specific endpoints
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//                        .requestMatchers( "/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
//                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//                        .requestMatchers( "/myCards").hasAuthority("VEIWCARDS")

                                .requestMatchers("/myAccount").hasRole("USER")
                                .requestMatchers( "/myBalance").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/myLoans").hasRole("USER")
                                .requestMatchers( "/myCards").hasRole("USER")
                        .requestMatchers( "/user").authenticated()
                        .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll()
                )

                // Enable default form-based login
                .formLogin(withDefaults())

                // HTTP Basic Authentication Configuration with custom entry point
                .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))

                // Exception Handling - Custom handlers for Access Denied and Authentication Failures
                .exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build(); // Build and return the configured SecurityFilterChain
    }

    /**
     * Configures a password encoder for securely hashing and verifying passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Supports multiple encoding algorithms
    }
}
