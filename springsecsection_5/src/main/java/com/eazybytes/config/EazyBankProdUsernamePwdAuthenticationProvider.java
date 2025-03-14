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
@Profile("prod")
// Use Lombok to automatically generate a constructor for the class
@RequiredArgsConstructor
public class EazyBankProdUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    // Inject the UserDetailsService and PasswordEncoder into the class
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    // Implement the authenticate method to handle username and password-based authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extract the username from the authentication object
        String username = authentication.getName();

        // Extract the password from the authentication object
        String pwd = authentication.getCredentials().toString();

        // Use the UserDetailsService to load the user details associated with the username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        // Compare the provided password with the stored password using the PasswordEncoder
        if(passwordEncoder.matches(pwd, userDetails.getPassword())){
            // If the passwords match, create a new UsernamePasswordAuthenticationToken
            // with the username, password, and user authorities
            return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
        }else{
            // If the passwords do not match, throw a BadCredentialsException
            throw new BadCredentialsException("Invalid password!");
        }
    }

    // Implement the supports method to check if the authentication object is an instance of UsernamePasswordAuthenticationToken
    @Override
    public boolean supports(Class<?> authentication) {
        // Return true if the authentication object is an instance of UsernamePasswordAuthenticationToken
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}