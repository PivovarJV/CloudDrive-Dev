package com.example.CloudFile.exception;

import com.example.CloudFile.dto.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ErrorMessageDTO> handlerInvalidPathException(InvalidPathException e){
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handlerResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorMessageDTO> handlerResourceConflictException(ResourceConflictException e) {
        return buildErrorResponse(e,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorMessageDTO> handlerStorageException(StorageException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessageDTO> handlerForbiddenException(ForbiddenException e) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleOther(Exception e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorMessageDTO> handleInvalidFileException(InvalidFileException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<ErrorMessageDTO> handleUserConflictException(UserConflictException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessageDTO> handleIllegalStateException(IllegalStateException e) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessageDTO> handleNoSuchElementException(NoSuchElementException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageDTO> handleAuthenticationException(AuthenticationException e) {
        ErrorMessageDTO messageDTO = new ErrorMessageDTO("Неверные данные (такого пользователя нет, " +
                "или пароль неправильный");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDTO);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessageDTO> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorMessageDTO> buildErrorResponse(Exception e, HttpStatus status) {
        ErrorMessageDTO messageDTO = new ErrorMessageDTO(e.getMessage());
        return ResponseEntity.status(status).body(messageDTO);
    }
}
