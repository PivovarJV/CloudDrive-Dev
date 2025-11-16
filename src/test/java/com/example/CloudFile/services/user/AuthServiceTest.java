package com.example.CloudFile.services.user;

import com.example.CloudFile.exception.UserConflictException;
import com.example.CloudFile.models.User;
import com.example.CloudFile.services.minio.util.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


class AuthServiceTest extends AbstractIntegrationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @MockBean
    MinioService minioService;

    @Test
    void registrationUser() {
        MockHttpSession session = new MockHttpSession();

        doNothing().when(minioService).createRootFolder(anyInt());

        User user = authService.registrationUser("Misha123", "1234", session);

        assertNotNull(user);
        assertEquals("Misha123", user.getLogin());

        Optional<User> userFromDb = userService.userByLogin("Misha123");
        assertEquals("Misha123", userFromDb.get().getLogin());

        verify(minioService, times(1)).createRootFolder(user.getId());
    }

    @Test
    void registrationUser_ShouldThrowExceptionWhenUsernameAlreadyExists() {
        String username = "Misha";
        String password = "1234";
        MockHttpSession session = new MockHttpSession();

        User user = new User(username, password);
        userService.saveUser(user);

        assertThrows(UserConflictException.class,
                () -> authService.registrationUser(username, password, session));
    }
}