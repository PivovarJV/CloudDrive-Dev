package com.example.CloudFile.services.minio.manager;

import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.services.minio.MinioService;
import com.example.CloudFile.util.UserPathProvider;
import com.example.CloudFile.validation.FileValidator;
import com.example.CloudFile.web.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
@RequiredArgsConstructor
@Service
public class UploadService {

    private final MinioService minioService;
    private final UserPathProvider pathProvider;

    public List<ObjectDTO> putObject(String path, MultipartFile[] files) {
        log.info("Начало загрузки файлов: {} в путь '{}'", files.length, pathProvider.rootPath() + path);
        FileValidator.validate(files);

        for (MultipartFile file : files) {
            if (minioService.exists(path + file.getOriginalFilename())) {
                log.error("Файл {} уже существует в {}", file.getOriginalFilename(), path);
                throw new ResourceConflictException("Файл " + file.getOriginalFilename() + " уже существует");
            }
        }

        ExecutorService executor = new DelegatingSecurityContextExecutorService(
                Executors.newFixedThreadPool(4));
        List<Future<ObjectDTO>> futures = new ArrayList<>();

        for (MultipartFile file : files) {
            futures.add(executor.submit(() ->
                    minioService.putObject(path + file.getOriginalFilename(), file)));
        }

        List<ObjectDTO> results = new ArrayList<>();
        for (Future<ObjectDTO> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                log.error("Ошибка при загрузке файла: {}", e.getMessage());
                throw new ResourceConflictException("Ошибка при загрузке файла: " + e.getCause().getMessage());
            }
        }
        executor.shutdown();
        log.info("Завершена загрузка файлов в '{}'. Загружено: {}", path, results.size());
        return results;
    }
}