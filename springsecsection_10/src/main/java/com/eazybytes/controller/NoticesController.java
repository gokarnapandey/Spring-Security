// Purpose: This controller handles requests for retrieving active notices from the database.

package com.eazybytes.controller;

import com.eazybytes.model.Notice;
import com.eazybytes.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

// Marks this class as a REST controller to handle HTTP requests.
@RestController

// Generates a constructor with required fields using Lombok for dependency injection.
@RequiredArgsConstructor
public class NoticesController {

    // Injects the NoticeRepository to interact with the database.
    private final NoticeRepository noticeRepository;

    // Handles GET requests at the "/notices" endpoint to retrieve active notices.
    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices() {

        // Retrieves all active notices from the database.
        List<Notice> notices = noticeRepository.findAllActiveNotices();

        // If active notices are found, return them with caching for 60 seconds.
        if (!notices.isEmpty()) {
            return ResponseEntity.ok()
                    // Sets cache control to store the response for 60 seconds, reducing server load.
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        } else {
            // Returns null if no active notices are found.
            return null;
        }
    }
}
