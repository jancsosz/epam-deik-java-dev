package com.epam.training.ticketservice.cliuser;

import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.model.Role;
import com.epam.training.ticketservice.repository.CliUserRepository;
import com.epam.training.ticketservice.service.CliUserService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CliUserServiceTest {

    private CliUser admin;

    @Mock
    CliUserRepository cliUserRepository;

    @InjectMocks
    CliUserService cliUserService;

    @BeforeEach
    private void createAdmin() {
        admin = CliUser.builder().username("admin")
                .password("admin")
                .role(Role.ROLE_ADMIN)
                .build();
    }

    @Test
    public void testGetAdminCliUserShouldReturnAdminAccountWhenFound() throws NotFoundException {
        // Given admin

        // When
        when(cliUserRepository.findUserByUsername(admin.getUsername())).thenReturn(admin);
        CliUser adminUser = cliUserService.getAdminCliUser();

        // Then
        Assertions.assertEquals(adminUser.getRole(), Role.ROLE_ADMIN);
    }

    @Test
    public void testGetAdminCliUserShouldThrowNotFoundExceptionWhenNotFound() throws NotFoundException {
        // Given


        // When

        // Then
        Assertions.assertThrows(NotFoundException.class,
                () -> cliUserService.getAdminCliUser());

    }
}
