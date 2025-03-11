// Purpose: This controller handles loan inquiries and retrieves loan details for a specific customer.

package com.eazybytes.controller;

import com.eazybytes.model.Loans;
import com.eazybytes.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Marks this class as a REST controller to handle HTTP requests.
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class LoansController {

    // Injects the LoanRepository to interact with the database.
    private final LoanRepository loanRepository;

    // Handles GET requests at the "/myLoans" endpoint to retrieve loan details for a specific customer.
    @GetMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestParam long id) {

        // Retrieves loan details for the specified customer ID, sorted by start date in descending order.
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);

        // Returns the list of loans if found; otherwise, returns null.
        if (!loans.isEmpty()) {
            return loans;
        } else {
            return null;
        }
    }
}
