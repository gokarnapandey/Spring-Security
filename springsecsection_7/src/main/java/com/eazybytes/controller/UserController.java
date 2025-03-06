package com.eazybytes.controller;


import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Handles user registration requests.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

        // Dependency injection for CustomerRepository and PasswordEncoder
        private final CustomerRepository customerRepository;
        private final PasswordEncoder passwordEncoder;

        /**
         * Handles HTTP POST requests to register a new user.
         *
         * @param customer The customer details to register.
         * @return A ResponseEntity with a success or error message.
         */
        @PostMapping("/register")
        public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
            try {
                // Hash the customer's password for secure storage
                String hashPwd = passwordEncoder.encode(customer.getPwd());
                customer.setPwd(hashPwd);

                // Save the customer to the database
                Customer savedCustomer = customerRepository.save(customer);

                // Check if the customer was successfully saved
                if (savedCustomer.getId() > 0) {
                    // Return a success response with a 201 Created status code
                    return ResponseEntity.status(HttpStatus.CREATED).body("Given user details are successfully registered");
                } else {
                    // Return an error response with a 400 Bad Request status code
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Registration failed");
                }
            } catch (Exception e) {
                // Return an error response with a 500 Internal Server Error status code
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurs " + e.getMessage());
            }
        }
}
