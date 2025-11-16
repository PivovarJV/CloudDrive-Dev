package com.example.CloudFile.services.user;

import com.example.CloudFile.models.CustomUserDetails;
import com.example.CloudFile.models.User;
import com.example.CloudFile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return new CustomUserDetails(user);
    }
}
