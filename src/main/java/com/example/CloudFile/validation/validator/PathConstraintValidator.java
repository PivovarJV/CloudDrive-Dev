package com.example.CloudFile.validation.validator;

import com.example.CloudFile.util.UserPathProvider;
import com.example.CloudFile.validation.annotation.ValidPath;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PathConstraintValidator implements ConstraintValidator<ValidPath, String> {

    private final UserPathProvider pathProvider;
    private final int MAX_LENGTH = 512;

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        String fullPath = pathProvider.rootPath() + path;
        log.info("Начало валидации пути: {}", fullPath);

        if (fullPath == null || fullPath.isBlank()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Невалидный или отсутствующий путь")
                    .addConstraintViolation();
            log.warn("Невалидный или отсутствующий путь");
            return false;
        }

        if (fullPath.length() > MAX_LENGTH) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Слишком длинный путь")
                    .addConstraintViolation();
            log.warn("Слишком длинный путь");
            return false;
        }

        if (fullPath.contains("..") || fullPath.contains("\\")
                || fullPath.contains(":") || fullPath.contains("//")) {

            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Путь содержит запрещённые символы")
                    .addConstraintViolation();
            log.warn("Путь содержит запрещённые символы");
            return false;
        }
        log.info("Валидация прошла успешно");
        return true;
    }
}
