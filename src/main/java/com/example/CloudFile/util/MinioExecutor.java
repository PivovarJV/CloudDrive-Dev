package com.example.CloudFile.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
@Component
public class MinioExecutor {

    private final MinioExceptionMapper minioExceptionMapper;

    public <T> T execute(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            throw minioExceptionMapper.map(e);
        }
    }
}
