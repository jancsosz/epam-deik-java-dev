package com.epam.training.ticketservice.cliuser;

import com.epam.training.ticketservice.commands.AuthCommands;
import com.epam.training.ticketservice.commands.CliUserCommands;
import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.service.CliUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CliUserCommandsTest {

    private CliUser admin;

    @Mock
    CliUserService cliUserService;

    @InjectMocks
    CliUserCommands cliUserCommands;

    @InjectMocks
    AuthCommands authCommands;

    @BeforeEach
    private void createAdmin() {
        admin = CliUser.builder().username("admin")
                .password("admin")
                .build();
    }

    @Test
    public void testDescribeAccountShouldReturnAdminDescriptionWhenSignedIn() throws Exception {
        // Given
        Authentication authentication = new TestingAuthenticationToken(
                admin.getUsername(),
                admin,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String adminDescription = "Signed in with privileged account 'admin'";

        // When
        when(cliUserService.getAdminCliUser()).thenReturn(admin);
        String descriptionResult = cliUserCommands.describeAccount();

        // Then
        Assertions.assertEquals(adminDescription, descriptionResult);
    }

    @Test
    public void testDescribeAccountShouldReturnYouAreNotSignedInIfNobodyHasSignedIn() throws Exception {

        // Given
        String expectedDescription = "You are not signed in";

        // When
        String actualDescription = cliUserCommands.describeAccount();

        // Then
        Assertions.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testSignOutShouldMakeSecurityContextNull() throws Exception {
        //Given
        Authentication authentication = new TestingAuthenticationToken(
                admin.getUsername(),
                admin,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        when(cliUserService.getAdminCliUser()).thenReturn(admin);
        String descriptionResult = cliUserCommands.describeAccount();
        authCommands.signOut();

        authentication = SecurityContextHolder.getContext().getAuthentication();

        // Then
        Assertions.assertNull(authentication);

    }
}
