package com.example.CloudFile.web.controller.api;

import com.example.CloudFile.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Auth API", description = "Операции авторизации и регистрации")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Регистрация пройдена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "409", description = "Username занят"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO registration(
            @Parameter(description = "DTO с username и password")
            @RequestBody UserDTO userDTO,
            HttpSession session);

    @Operation(summary = "Авторизация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторизация пройдена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "401", description = "Неверные данные (такого пользователя нет, или пароль неправильный)"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/sign-in")
   UserDTO login(
           @Parameter(description = "DTO с username и password")
           @RequestBody UserDTO userDTO,
           HttpSession session);
}
