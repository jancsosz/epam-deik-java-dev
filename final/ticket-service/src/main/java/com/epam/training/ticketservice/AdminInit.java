package com.epam.training.ticketservice;

import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.model.Role;
import com.epam.training.ticketservice.repository.CliUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminInit {

    private final CliUserRepository cliUserRepository;

    @PostConstruct
    private void initAdmin() {

        CliUser admin = CliUser.builder()
                .username("admin")
                .password("admin")
                .role(Role.ROLE_ADMIN)
                .build();

        cliUserRepository.save(admin);

    }


}
