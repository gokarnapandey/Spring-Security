package com.eazybytes.events;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {

// Purpose: This code listens to authentication success and failure events in Spring Security
// and logs relevant information about login attempts.

    // Listens for successful authentication events and logs the username.
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent successEvent){
        // Logs a message indicating a successful login for the user.
        log.info("Login is successful for user : {}", successEvent.getAuthentication().getName());
    }

    // Listens for authentication failure events and logs the failure reason.
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failureEvent){
        // Logs a message indicating a failed login attempt, including the username
        // and the reason for failure.
        log.error("Login Fails for the user : {} due to : {}",
                failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }

}
