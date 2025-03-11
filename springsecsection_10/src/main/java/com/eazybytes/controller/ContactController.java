// Purpose: This controller handles contact inquiry details and saves them in the database.

package com.eazybytes.controller;

import com.eazybytes.model.Contact;
import com.eazybytes.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Random;

// Marks this class as a REST controller, making it ready to handle web requests.
@RestController

// Generates a constructor with required fields using Lombok.
@RequiredArgsConstructor
public class ContactController {

    // Injects the ContactRepository dependency to interact with the database.
    private final ContactRepository contactRepository;

    // Handles POST requests at the "/contact" endpoint to save contact inquiry details.
    @PostMapping("/contact")
    public Contact saveContactInquiryDetails(@RequestBody Contact contact) {
        // Generates a unique service request number for the contact.
        contact.setContactId(getServiceReqNumber());

        // Sets the current system date as the creation date for the contact.
        contact.setCreateDt(new Date(System.currentTimeMillis()));

        // Saves the contact details in the database and returns the saved object.
        return contactRepository.save(contact);
    }

    // Generates a unique service request number for each contact.
    public String getServiceReqNumber() {
        Random random = new Random();

        // Generates a random number between 9999 and 999999999 for uniqueness.
        int ranNum = random.nextInt(999999999 - 9999) + 9999;

        // Prefixes the generated number with "SR" to indicate a service request ID.
        return "SR" + ranNum;
    }
}
