package com.example.CloudFile.validation;

import com.example.CloudFile.exception.InvalidPathException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathValidator {
    private static final int MAX_LENGTH = 512;

    public static void validate(String path) {
        if (path == null || path.isBlank()) {
            throw new InvalidPathException("Невалидный или отсутствующий путь");
        }
        if (path.length() > MAX_LENGTH) {
            throw new InvalidPathException("Слишком длинный путь");
        }
        if (path.contains("..") || path.contains("\\") || path.contains(":") || path.contains("//")) {
            throw new InvalidPathException("Путь содержит запрещённые символы");
        }
    }

    public static void validate(String from, String to) {
        validate(from);
        validate(to);
    }
}
