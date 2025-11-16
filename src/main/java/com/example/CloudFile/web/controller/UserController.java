package com.example.CloudFile.web.controller;

import com.example.CloudFile.web.controller.api.UserApi;
import com.example.CloudFile.dto.UserDTO;
import com.example.CloudFile.models.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    @Override
    public UserDTO getProfile(CustomUserDetails userDetails) {
        return  userDetails.getUser().toDTO();
    }
}
