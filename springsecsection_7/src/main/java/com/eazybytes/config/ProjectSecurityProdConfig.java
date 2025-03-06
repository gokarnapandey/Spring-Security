// This package contains configuration classes for the application
package com.eazybytes.config;

// Import necessary Spring Security and configuration annotations
import com.eazybytes.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Import a static method for customizing security configurations with default settings
import static org.springframework.security.config.Customizer.withDefaults;

// This configuration class is specific to the production environment
@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    // Define a bean for the security filter chain
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // Purpose: Configures session management in Spring Security to handle invalid and expired sessions,
        // and restricts the maximum number of concurrent sessions per user.
        http.sessionManagement(
                smc ->
                        // Redirects to "/invalidSession" when an invalid session is detected.
                        // This ensures users are notified if their session becomes invalid.
                        smc.invalidSessionUrl("/invalidSession")

                                // Limits the maximum number of concurrent sessions per user to 3.
                                // If a user tries to log in more than 3 times simultaneously, session control policies will apply.
                                .maximumSessions(1)

                                // Prevents new logins when the maximum session limit is reached.
                                // If a user already has 3 active sessions, further login attempts will be blocked.
                                .maxSessionsPreventsLogin(true)

                                // Redirects to "/expiredURL" when a session expires due to session limits.
                                // This informs users that their previous session has been terminated.
                                .expiredUrl("/expiredURL")
        );


        // Require secure channels (HTTPS) for all requests
        http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure())

                // Disable CSRF protection
                .csrf((csrf) -> csrf.disable())

                // Restrict access to certain paths to authenticated users only
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/myAccount", "/myLoans", "/myCards", "/myBalance").authenticated());

        // Allow public access to other paths
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll());

        // Enable form login with default settings
        http.formLogin(withDefaults());

        // Configure HTTP Basic authentication
        http.httpBasic(
                hbc ->
                        // Set a custom authentication entry point for HTTP Basic authentication
                        hbc.authenticationEntryPoint(
                                // Create a new instance of the custom authentication entry point
                                new CustomBasicAuthenticationEntryPoint()
                        )
        );

// Configure exception handling for HTTP security
        http.exceptionHandling(
                ehc ->
                        // Set a custom access denied handler for handling access denied exceptions
                        ehc.accessDeniedHandler(
                                // Create a new instance of the custom access denied handler
                                new CustomAccessDeniedHandler()
                        )

        );

        // Build and return the security filter chain
        return http.build();
    }

    // Define a bean for the password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Create a delegating password encoder for password hashing and verification
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}