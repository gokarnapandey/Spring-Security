package com.eazybytes.config;

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


// This is a Spring Security configuration class
@Configuration
// This configuration will only be active when the "prod" profile is not active
@Profile(" !prod ")
public class ProjectSecurityConfig {

    // This method creates a SecurityFilterChain bean, which is a core component of Spring Security
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection
        http.csrf((csrf) -> csrf.disable());

        // Configure authorization for specific paths
        // Only allow authenticated requests for these paths
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/myAccount","/myLoans","/myCards","/myBalance").authenticated());

        // Allow all requests for these paths (no authentication required)
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/notices","/contact","/error","/register").permitAll());

        // Enable form login with default settings
        http.formLogin(withDefaults());

        // Enable HTTP basic authentication with default settings
        http.httpBasic(withDefaults());

        // Build and return the SecurityFilterChain
        return http.build();
    }

    // This method creates a PasswordEncoder bean, which is used to hash and verify passwords
    @Bean
    public PasswordEncoder passwordEncoder(){
        // Create a delegating password encoder that can handle different types of encoders
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}