package com.example.CloudFile.util;

import com.example.CloudFile.exception.ForbiddenException;
import com.example.CloudFile.exception.ResourceConflictException;
import com.example.CloudFile.exception.ResourceNotFoundException;
import com.example.CloudFile.exception.StorageException;
import io.minio.errors.ErrorResponseException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public  class MinioExceptionMapper {
    public RuntimeException map(Exception e) {
        Throwable root = findRootCause(e);
        if (root instanceof ErrorResponseException errorEx) {
            String code = errorEx.errorResponse().code();

            return switch (code) {
                case "NoSuchKey" -> new ResourceNotFoundException("Файл не найден.");
                case "NoSuchBucket" -> new ResourceNotFoundException("Бакет не найден.");
                case "AccessDenied" -> new ForbiddenException("Нет доступа.");
                case "BucketAlreadyExists" -> new ResourceConflictException("Данный бакет уже создан");
                default -> new StorageException("Ошибка MinIO: " + code);
            };
        }
        if (e instanceof IOException) {
            return new StorageException("Проблема с сетью или файловой системой");
        }
        return new StorageException("Неизвестная ошибка :(");
    }

    private Throwable findRootCause(Throwable t) {
        Throwable result = t;
        while (result.getCause() != null) {
            result = result.getCause();
        }
        return result;
    }
}
