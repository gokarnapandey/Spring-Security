package com.eazybytes.config;

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

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).
//                if Optional of customer object does not exist then this line of code is going to be executed with provided exception.
                orElseThrow( ( ) -> new UsernameNotFoundException("User Details not found for the user " + username));


// Create a list of authorities (roles) granted to the user
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(customer.getRole()) // Create a single authority based on the customer's role
        );

// Create a new User object with the customer's credentials and authorities
        return new User(
                customer.getEmail(), // Use the customer's email as the username
                customer.getPwd(), // Use the customer's password
                authorities // Assign the list of authorities to the user
        );
    }
}
