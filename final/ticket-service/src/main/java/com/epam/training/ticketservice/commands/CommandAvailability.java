package com.epam.training.ticketservice.commands;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.Availability;

public class CommandAvailability {

    public Availability isAccountAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return Availability.unavailable("you have to be signed in with admin to use this command");
        }
        if (authentication.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ROLE_ADMIN"))) {
            return Availability.unavailable("this is not an admin account");
        }
        return Availability.available();
    }

    public Availability isNotSignedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return Availability.available();
        } else {
            return Availability.unavailable("You are already signed in");
        }
    }
}
