package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User describeAdminAccount() {
        return userRepository.findUserByUsername("admin");
    }
}
