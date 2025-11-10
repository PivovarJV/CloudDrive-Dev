package com.example.CloudFile.util;

import com.example.CloudFile.models.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserPathProvider {

    public String rootPath() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        int userId = principal.getUser().getId();
        return "user-" + userId + "-files/";
    }
}
