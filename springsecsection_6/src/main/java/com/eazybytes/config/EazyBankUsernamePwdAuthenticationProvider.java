// Define a custom authentication provider for Spring Security
package com.eazybytes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Indicate that this class is a Spring component
@Component
// This profile annotation suggests that this authentication provider is not used in production
@Profile(" !prod ")
// Use Lombok to automatically generate a constructor for the class
@RequiredArgsConstructor
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    // Inject the UserDetailsService and PasswordEncoder into the class
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder; // Not used in this implementation


    // Implement the authenticate method to handle username and password-based authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extract the username from the authentication object
        String username = authentication.getName();

        // Extract the password from the authentication object
        String pwd = authentication.getCredentials().toString();

        // Use the UserDetailsService to load the user details associated with the username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Typically, you would use the PasswordEncoder to compare the provided password with the stored password
        // However, in this implementation, it simply returns a new authentication token without verifying the password
        // You should add password validation here to make this implementation secure
        return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
    }

    // Implement the supports method to check if the authentication object is an instance of UsernamePasswordAuthenticationToken
    @Override
    public boolean supports(Class<?> authentication) {
        // Return true if the authentication object is an instance of UsernamePasswordAuthenticationToken
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}