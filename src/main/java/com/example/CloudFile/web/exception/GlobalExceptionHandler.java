package com.example.CloudFile.web.exception;

import com.example.CloudFile.dto.ErrorMessageDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPathException.class)
    public ErrorMessageDTO handlerInvalidPathException(InvalidPathException e){
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorMessageDTO handlerResourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceConflictException.class)
    public ErrorMessageDTO handlerResourceConflictException(ResourceConflictException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StorageException.class)
    public ErrorMessageDTO handlerStorageException(StorageException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorMessageDTO handlerForbiddenException(ForbiddenException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorMessageDTO handleOther(Exception e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFileException.class)
    public ErrorMessageDTO handleInvalidFileException(InvalidFileException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserConflictException.class)
    public ErrorMessageDTO handleUserConflictException(UserConflictException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorMessageDTO handleIllegalStateException(IllegalStateException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorMessageDTO handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorMessageDTO handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessageDTO handleAuthenticationException() {
        return new ErrorMessageDTO("Неверные данные (такого пользователя нет, " +
                "или пароль неправильный)");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorMessageDTO handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ErrorMessageDTO(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorMessageDTO handleHandlerMethodValidation(HandlerMethodValidationException e) {

        String message = e.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return new ErrorMessageDTO(message);
    }

}
