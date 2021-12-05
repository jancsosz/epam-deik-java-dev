package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.service.CliUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class CliUserCommands {

    private final CliUserService cliUserService;

    @ShellMethod(value = "describe account", key = "describe account")
    public String describeAccount() throws Exception {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "Signed in with privileged account '" + cliUserService.getAdminCliUser().getUsername() + "'";
        } else {
            return "You are not signed in";
        }
    }
}
