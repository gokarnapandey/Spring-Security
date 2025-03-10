// Purpose: This controller handles balance inquiries and retrieves account transaction details.

package com.eazybytes.controller;

import com.eazybytes.model.AccountTransactions;
import com.eazybytes.repository.AccountTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Marks this class as a REST controller to handle HTTP requests.
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class BalanceController {

    // Injects the AccountTransactionsRepository to interact with the database.
    private final AccountTransactionsRepository accountTransactionsRepository;

    // Handles GET requests at the "/myBalance" endpoint to retrieve balance details.
    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam long id) {

        // Retrieves account transactions for the specified customer ID, sorted by transaction date in descending order.
        List<AccountTransactions> accountTransactions = accountTransactionsRepository
                .findByCustomerIdOrderByTransactionDtDesc(id);

        // Returns the list of transactions if found; otherwise, returns null.
        if (accountTransactions != null) {
            return accountTransactions;
        } else {
            return null;
        }
    }
}
