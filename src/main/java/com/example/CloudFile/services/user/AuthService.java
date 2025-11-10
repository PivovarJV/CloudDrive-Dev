package com.example.CloudFile.services.user;

import com.example.CloudFile.exception.UserConflictException;
import com.example.CloudFile.models.CustomUserDetails;
import com.example.CloudFile.models.User;
import com.example.CloudFile.services.minio.util.MinioService;
import com.example.CloudFile.validation.CredentialsValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userServices;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MinioService minioService;

    public User registrationUser(String username, String password, HttpSession session) {
        log.info("Начало регистрации юзера: {} ", username);
        CredentialsValidator.validateUsername(username);
        CredentialsValidator.validatePassword(password);
        log.info("Валидация прошла успешна");
        if (userServices.existsByLogin(username)) {
            log.warn("Username {} уже занят", username);
            throw new UserConflictException("Username уже занят");
        }
        String hashPassword = passwordEncoder.encode(password);
        User user = new User(username, hashPassword);
        log.info("User: {} создан", username);
        userServices.saveUser(user);
        log.info("User: {} сохранен в БД", username);
        minioService.createRootFolder(user.getId());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);

        return user;
    }


    public User loginUser(String username, String password, HttpSession session) {
        log.info("Начало входа юзера: {} ", username);
        CredentialsValidator.validateUsername(username);
        CredentialsValidator.validatePassword(password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context);
        log.info("{} успешно вошел в систему", username);
        return ((CustomUserDetails) auth.getPrincipal()).getUser();
    }
}
