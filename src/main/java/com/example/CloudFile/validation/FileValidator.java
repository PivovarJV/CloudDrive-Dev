package com.example.CloudFile.validation;

import com.example.CloudFile.web.exception.InvalidFileException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileValidator {

    public static void validate(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new InvalidFileException("Файлы для загрузки отсутствуют");
        }
        if (files.length > 10) {
            throw new InvalidFileException("Слишком много файлов для загрузки (максимум 10)");
        }
        for (MultipartFile file : files) {
            validate(file);
        }
    }

    private static void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Один из файлов пуст или не выбран");
        }
        String name = file.getOriginalFilename();
        if (name == null || name.isBlank()) {
            throw new InvalidFileException("Файл без имени");
        }
        if (name.length() > 155) {
            throw new InvalidFileException("Имя файла слишком длинное");
        }
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new InvalidFileException("Размер файла превышает 50 МБ: " + name);
        }
    }
}
