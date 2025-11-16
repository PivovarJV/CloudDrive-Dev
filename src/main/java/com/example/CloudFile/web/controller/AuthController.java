package com.example.CloudFile.web.controller;

import com.example.CloudFile.web.controller.api.AuthApi;
import com.example.CloudFile.dto.UserDTO;
import com.example.CloudFile.services.user.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public UserDTO registration(UserDTO userDTO, HttpSession session) {
        return authService.registrationUser(userDTO.getUsername(), userDTO.getPassword(), session).toDTO();
    }

    @Override
    public UserDTO login(UserDTO userDTO, HttpSession session) {
        return authService.loginUser(userDTO.getUsername(), userDTO.getPassword(), session).toDTO();
    }
}

