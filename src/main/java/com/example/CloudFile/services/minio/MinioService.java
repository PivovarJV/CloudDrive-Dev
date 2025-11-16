package com.example.CloudFile.services.minio;


import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.mapper.ObjectDTOMapper;
import com.example.CloudFile.util.MinioExceptionMapper;
import com.example.CloudFile.util.MinioExecutor;
import com.example.CloudFile.util.UserPathProvider;
import com.example.CloudFile.web.exception.ResourceConflictException;
import com.example.CloudFile.web.exception.ResourceNotFoundException;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MinioService {

    private final String BUCKET_NAME = "user-files";
    private final MinioClient minioClient;
    private final MinioExceptionMapper minioExceptionMapper;
    private final MinioExecutor executor;
    private final ObjectDTOMapper mapper;
    private final UserPathProvider pathProvider;

    public void removeObject(String path) {
        log.info("Удаление объекта по пути: {}", pathProvider.rootPath() + path);
        statObject(path);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(pathProvider.rootPath() + path)
                            .build());
            log.info("Объект по пути {} успешно удален", pathProvider.rootPath() + path);
        } catch (Exception e) {
            log.warn("Ошибка при удалении объекта по пути: {}", pathProvider.rootPath() + path);
            throw minioExceptionMapper.map(e);
        }
    }

    public Iterable<Result<Item>> listFolderContent(String path) {
        log.info("Загрузка списка файлов по пути {}", pathProvider.rootPath() + path);
        return executor.execute(() ->
                minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(pathProvider.rootPath() + path)
                        .recursive(true)
                        .build()));
    }

    public List<ObjectDTO> search(String query) {
        log.info("Начало поиска по запросу: {}", query);
        List<ObjectDTO> results = new ArrayList<>();
        Iterable<Result<Item>> resultItem = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(pathProvider.rootPath())
                        .recursive(true)
                        .build());
        List<ObjectDTO> objectDTOList = mapper.mapResultsToDTOs(resultItem, query, pathProvider.rootPath());
        for (ObjectDTO objectDTO : objectDTOList) {
            if (objectDTO.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(objectDTO);
            }
        }
        log.info("В результате поиска нашлось: {} объекта", results.size());
        return results;
    }

    public ObjectDTO statObject(String path)  {
        log.info("Информация объекта находящегося по пути {}", pathProvider.rootPath() + path);
        String folderPath = path.substring(0, path.lastIndexOf("/") + 1);
        String name = path.substring(path.lastIndexOf("/") + 1);
        StatObjectResponse stat = executor.execute(() -> minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(pathProvider.rootPath() + path)
                        .build()));
        return new ObjectDTO(folderPath, name, stat.size(),"FILE");
    }

    public InputStream getObject(String path) {
        log.info("Загрузка потока {}", path);
        return executor.execute(() -> minioClient.getObject(GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(path)
                .build()));
    }

    public ObjectDTO putObject(String path, MultipartFile file)  {
        log.info("Начало загрузки файла: {} в путь {}", file.getOriginalFilename(), pathProvider.rootPath() + path);
        executor.execute(() -> minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(pathProvider.rootPath() + path)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()));
        return statObject(path);
    }

    public ObjectDTO createFolder(String path) {
        log.info("Создание папки по пути: {}", pathProvider.rootPath() + path);
        if (exists(path)) {
            throw new ResourceConflictException("Папка уже существует");
        }
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        executor.execute(() -> minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(pathProvider.rootPath() + path)
                        .stream(inputStream, 0, -1)
                        .build()));
        log.info("Папка по пути {} успешно создана", path);
        return new ObjectDTO(path, "DIRECTORY");
    }

    public void createRootFolder(int id) {
        String rootPath = "user-" + id + "-files/";
        log.info("Создание корневой папки папки по пути: {}", rootPath);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        executor.execute(() -> minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(rootPath)
                        .stream(inputStream, 0, -1)
                        .build()));
    }

    public ObjectDTO copyObject(String from, String to)  {
        log.info("Копирование объекта из {} в {}", pathProvider.rootPath() + from, pathProvider.rootPath() +to);
        if (exists(to)) {
            throw new ResourceConflictException("Файл уже существует");
        }
        executor.execute(() -> minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(pathProvider.rootPath() + to)
                        .source(
                                CopySource.builder()
                                        .bucket(BUCKET_NAME)
                                        .object(pathProvider.rootPath() + from)
                                        .build())
                        .build()));
        log.info("Удаление объекта из {}",pathProvider.rootPath() + from);
        removeObject(from);
        return statObject(to);
    }

    public boolean exists(String path) {
        try {
            statObject(path);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    public List<ObjectDTO> listObjects(String folder) {
        log.info("Начало загрузки списка файлов находящихся по пути: {}", pathProvider.rootPath() + folder);
        if (!folderExists(folder)) {
            log.warn("Несуществующий путь: {}", folder);
            throw new ResourceNotFoundException("Несуществующий путь");
        }
        Iterable<Result<Item>> results = executor.execute(() ->
                minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(BUCKET_NAME)
                                .prefix(pathProvider.rootPath() + folder)
                                .build()));
        log.info("Успешная загрузка списка файлов находящихся по пути: {}", pathProvider.rootPath() + folder);
        return mapper.mapResultsToDTOs(results, folder, pathProvider.rootPath());
    }

    private boolean folderExists(String path) {
        Iterable<Result<Item>> items = listFolderContent(path);
        return items.iterator().hasNext();
    }
}