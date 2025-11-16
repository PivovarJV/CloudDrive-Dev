package com.example.CloudFile.controller;

import com.example.CloudFile.dto.UserDTO;
import com.example.CloudFile.models.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "Операции с пользователем")
@RequestMapping("/api/v1/user")
public class UserController {

    @Operation(summary = "Профиль пользователя")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Профиль показан"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getProfile(
            @Parameter(description = "Кастомная DTO для Spring Security")
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDetails.getUser().toDTO());
    }

}
