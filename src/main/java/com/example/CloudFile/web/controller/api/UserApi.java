package com.example.CloudFile.web.controller.api;

import com.example.CloudFile.dto.UserDTO;
import com.example.CloudFile.models.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User API", description = "Операции с пользователем")
@RequestMapping("/api/v1/user")
public interface UserApi {
    @Operation(summary = "Профиль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль показан"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/me")
    UserDTO getProfile(
            @Parameter(description = "Кастомная DTO для Spring Security")
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
