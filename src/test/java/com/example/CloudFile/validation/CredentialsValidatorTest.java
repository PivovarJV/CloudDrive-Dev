package com.example.CloudFile.validation;

import com.example.CloudFile.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class CredentialsValidatorTest {

    @Test
    void normUsername() {
        Assertions.assertDoesNotThrow(() ->
                CredentialsValidator.validateUsername("Misha"));
    }

    @Test
    void shouldThrowWhenUsernameIsBlank() {
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> CredentialsValidator.validateUsername("")
        );
        Assertions.assertEquals("Имя пользователя не может быть пустым", exception.getMessage());
    }

    @Test
    void longUsername() {
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> CredentialsValidator.validateUsername("Mi")
        );
        Assertions.assertEquals("Имя пользователя должно быть от 5 до 20 символов", exception.getMessage());
    }

    @Test
    void maxDlinaUsername() {
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> CredentialsValidator.validateUsername("a".repeat(21))
        );
        Assertions.assertEquals("Имя пользователя должно быть от 5 до 20 символов", exception.getMessage());
    }
}
