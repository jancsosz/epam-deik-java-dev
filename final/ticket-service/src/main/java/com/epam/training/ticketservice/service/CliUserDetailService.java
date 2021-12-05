package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.model.Role;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CliUserDetailService implements UserDetailsService {

    private final CliUserService cliUserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            CliUser user = cliUserService.getAdminCliUser();

            User.UserBuilder builder = User.withUsername(userName);
            builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));

            if (user.getRole().equals(Role.ROLE_ADMIN)) {
                builder.roles("USER", "ADMIN");
            } else if (user.getRole().equals(Role.ROLE_CLIENT)) {
                builder.roles("USER");
            }


            return builder.build();
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("User does not exist");
        }
    }
}
