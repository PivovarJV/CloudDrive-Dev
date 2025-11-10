package com.example.CloudFile.services.user;

import com.example.CloudFile.models.User;
import com.example.CloudFile.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> userById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> userByLogin(String login) {
        if (login == null) {
            throw new IllegalStateException("Данного Login нет");
        }
        return userRepository.findByLogin(login);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public User getProfile(HttpSession session) {
        Integer idUser = (Integer) session.getAttribute("userId");
        if (idUser == null) {
            throw new IllegalStateException("Данного UserId нет в session");
        }
        return userById(idUser)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Пользователь с id %s не найден", idUser)));
    }
}
