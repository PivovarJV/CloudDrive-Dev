package com.example.CloudFile.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageDTO {
    private String message;

    public ErrorMessageDTO(String message) {
        this.message = message;
    }
}
