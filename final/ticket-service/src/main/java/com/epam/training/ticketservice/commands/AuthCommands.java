package com.epam.training.ticketservice.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class AuthCommands extends CommandAvailability {

    private final AuthenticationManager authenticationManager;

    @ShellMethod(value = "sign in privileged username password", key = "sign in privileged")
    @ShellMethodAvailability("isNotSignedIn")
    public String signInAdmin(String userName, String password) {

        Authentication request = new UsernamePasswordAuthenticationToken(userName, password);

        try {
            Authentication result = authenticationManager.authenticate(request);
            if (result.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"))) {
                SecurityContextHolder.getContext().setAuthentication(result);
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            return "Login failed due to incorrect credentials";
        }
        return null;
    }


    @ShellMethod(value = "sign out", key = "sign out")
    public void signOut() {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
