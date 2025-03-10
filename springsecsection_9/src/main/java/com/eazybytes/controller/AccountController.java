// Purpose: This controller handles customer account inquiries and retrieves account details.

package com.eazybytes.controller;

import com.eazybytes.model.Accounts;
import com.eazybytes.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Marks this class as a REST controller to handle HTTP requests.
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class AccountController {

    // Injects the AccountsRepository to interact with the database.
    private final AccountsRepository accountsRepository;

    // Handles GET requests at the "/myAccount" endpoint to retrieve account details.
    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam long id) {

        // Retrieves account details for the specified customer ID.
        Accounts accounts = accountsRepository.findByCustomerId(id);

        // Returns the account details if found; otherwise, returns null.
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }
}
