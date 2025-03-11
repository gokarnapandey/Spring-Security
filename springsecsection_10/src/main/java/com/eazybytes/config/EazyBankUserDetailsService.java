// Define the package for this class
package com.eazybytes.config;

// Import necessary classes and interfaces
import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Mark this class as a Spring Service
@Service
// Use Lombok's @RequiredArgsConstructor to inject the CustomerRepository instance
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {

    // Inject the CustomerRepository instance
    private final CustomerRepository customerRepository;

    // Implement the loadUserByUsername method from the UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use the CustomerRepository to find a customer by email (username)
        Customer customer = customerRepository.findByEmail(username)
                // If the customer is not found, throw a UsernameNotFoundException
                .orElseThrow(() -> new UsernameNotFoundException("User Details not found for the user " + username));


        List<GrantedAuthority> authorities =  customer.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        // Create a new User object with the customer's credentials and authorities
        return new User(
                customer.getEmail(), // Use the customer's email as the username
                customer.getPwd(), // Use the customer's password
                authorities // Assign the list of authorities to the user
        );
    }
}