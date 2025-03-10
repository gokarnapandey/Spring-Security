// Purpose: This controller handles user registration requests, securely storing customer details.

package com.eazybytes.controller;

import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

/**
 * REST controller that manages user registration functionality.
 */
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class UserController {

    // Injects the CustomerRepository for database interactions.
    private final CustomerRepository customerRepository;

    // Injects the PasswordEncoder for securely hashing passwords.
    private final PasswordEncoder passwordEncoder;

    /**
     * Handles HTTP POST requests to register a new user.
     *
     * @param customer The customer details to be registered.
     * @return A ResponseEntity containing a success or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            // Encrypt the customer's password before saving it to the database.
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);

            // Set the account creation date to the current date and time.
            customer.setCreateDt(new Date(System.currentTimeMillis()));

            // Save the customer details to the database.
            Customer savedCustomer = customerRepository.save(customer);

            // Return success response if the customer was successfully registered.
            if (savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            } else {
                // Return error response if customer registration fails.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User Registration failed");
            }
        } catch (Exception e) {
            // Return error response if an exception occurs during registration.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred: " + e.getMessage());
        }
    }

    /**
     * Purpose: Retrieves customer details for the authenticated user after a successful login.
     *
     * @param authentication Authentication object containing the logged-in user's details.
     * @return The authenticated customer's details or null if not found.
     */
    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {

        // Retrieves customer details based on the authenticated user's email.
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());

        // Returns customer details if present; otherwise, returns null.
        return optionalCustomer.orElse(null);
    }
}
