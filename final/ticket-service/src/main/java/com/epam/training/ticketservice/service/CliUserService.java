package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.repository.CliUserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CliUserService {

    private final CliUserRepository cliUserRepository;

    public CliUser getAdminCliUser() throws NotFoundException {

        CliUser cliUser = cliUserRepository.findUserByUsername("admin");

        if (cliUser != null) {
            return cliUser;
        } else {
            throw new NotFoundException("User does not exist.");
        }
    }

    public List<CliUser> listAccounts() {
        return cliUserRepository.findAll();
    }

    public void createAccount(CliUser cliUser) {
        cliUserRepository.save(cliUser);
    }
}
