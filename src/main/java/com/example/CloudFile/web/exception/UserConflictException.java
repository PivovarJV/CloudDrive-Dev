package com.example.CloudFile.web.exception;

public class UserConflictException extends RuntimeException{
    public UserConflictException(String message) {
        super(message);
    }
}
