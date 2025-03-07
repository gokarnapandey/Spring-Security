package com.eazybytes.config;

import com.eazybytes.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

// Configuration class for Spring Security
@Configuration
// Activate this configuration only when the "prod" profile is not active
@Profile(" !prod ")
public class ProjectSecurityConfig {

    // Define a SecurityFilterChain bean to configure security settings
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
                                .maximumSessions(3)

                                // Prevents new logins when the maximum session limit is reached.
                                // If a user already has 3 active sessions, further login attempts will be blocked.
                                .maxSessionsPreventsLogin(true)

                                // Redirects to "/expiredURL" when a session expires due to session limits.
                                // This informs users that their previous session has been terminated.
                                .expiredUrl("/expiredURL")
        );


        // Allow insecure channel for all requests
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                // Disable CSRF protection
                .csrf((csrf) -> csrf.disable());

        // Restrict access to certain paths to authenticated users only
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/myAccount", "/myLoans", "/myCards", "/myBalance").authenticated());

        // Allow public access to certain paths
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession").permitAll());

        // Enable form login with default settings
        http.formLogin(withDefaults());

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


        // Build and return the SecurityFilterChain
        return http.build();
    }

    // Define a PasswordEncoder bean to handle password hashing and verification
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Create a delegating password encoder to support multiple encoding algorithms
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}