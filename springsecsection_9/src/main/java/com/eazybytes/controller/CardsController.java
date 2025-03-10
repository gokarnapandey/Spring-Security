// Purpose: This controller handles credit/debit card inquiries and retrieves card details
// for a specific customer.

package com.eazybytes.controller;

import com.eazybytes.model.Cards;
import com.eazybytes.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Marks this class as a REST controller to handle HTTP requests.
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class CardsController {

    // Injects the CardsRepository to interact with the database.
    private final CardsRepository cardsRepository;

    // Handles GET requests at the "/myCards" endpoint to retrieve card details for a specific customer.
    @GetMapping("/myCards")
    public List<Cards> getCardsDetails(@RequestParam long id) {

        // Retrieves the list of cards associated with the specified customer ID.
        List<Cards> cards = cardsRepository.findByCustomerId(id);

        // Returns the list of cards if found; otherwise, returns null.
        if (!cards.isEmpty())
            return cards;
        else
            return null;
    }
}
