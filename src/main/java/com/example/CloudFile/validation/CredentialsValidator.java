package com.example.CloudFile.validation;

import com.example.CloudFile.web.exception.InvalidCredentialsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CredentialsValidator {
    private static final Logger log = LoggerFactory.getLogger(CredentialsValidator.class);
    private static final int USERNAME_MIN = 5;
    private static final int USERNAME_MAX = 20;
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9]+[a-zA-Z_0-9]*[a-zA-Z0-9]+$");

    private static final int PASSWORD_MIN = 4;
    private static final int PASSWORD_MAX = 20;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>\\[\\]/`~+=\\-_';]*$");

    public static void validateUsername(String username) {
        log.info("Начало валидации username: {}", username);
        if (username == null || username.isBlank()) {
            log.warn("Имя пользователя не может быть пустым");
            throw new InvalidCredentialsException("Имя пользователя не может быть пустым");
        }
        if (username.length() < USERNAME_MIN || username.length() > USERNAME_MAX) {
            log.warn("Имя пользователя некорректного размера");
            throw new InvalidCredentialsException("Имя пользователя должно быть от " + USERNAME_MIN + " до " + USERNAME_MAX + " символов");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            log.warn("Имя пользователя содержит недопустимые символы");
            throw new InvalidCredentialsException("Имя пользователя содержит недопустимые символы");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            log.warn("Пароль не может быть пустым");
            throw new InvalidCredentialsException("Пароль не может быть пустым");
        }
        if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
            log.warn("Пароль пользователя некорректного размера");
            throw new InvalidCredentialsException("Пароль должен быть от " + PASSWORD_MIN + " до " + PASSWORD_MAX + " символов");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            log.warn("Пароль пользователя содержит недопустимые символы");
            throw new InvalidCredentialsException("Пароль содержит недопустимые символы");
        }
    }
}
