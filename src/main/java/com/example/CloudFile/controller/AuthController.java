package com.example.CloudFile.controller;

import com.example.CloudFile.dto.UserDTO;
import com.example.CloudFile.services.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth API", description = "Операции авторизации и регистрации")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Регистрация пройдена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "409", description = "Username занят"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> registration(
            @Parameter(description = "DTO с username и password")
            @RequestBody UserDTO userDTO,
            HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registrationUser(userDTO.getUsername(), userDTO.getPassword(), session).toDTO());
    }

    @Operation(summary = "Авторизация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторизация пройдена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "401", description = "Неверные данные (такого пользователя нет, или пароль неправильный)"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> login(
            @Parameter(description = "DTO с username и password")
            @RequestBody UserDTO userDTO,
            HttpSession session) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.loginUser(userDTO.getUsername(), userDTO.getPassword(), session).toDTO());
    }
}
