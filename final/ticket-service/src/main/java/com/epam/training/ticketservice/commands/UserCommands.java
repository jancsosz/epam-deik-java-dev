package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class UserCommands {

    private final UserService userService;

    @ShellMethod(value = "describe account", key = "describe account")
    @ShellMethodAvailability("isAccountAdmin")
    public String describeAccount() throws Exception {

        User admin;

        try {
            admin = userService.describeAdminAccount();

        } catch (Exception e) {
            return e.getMessage();
        }

        return "Signed in with privileged account " + admin;
    }
}
