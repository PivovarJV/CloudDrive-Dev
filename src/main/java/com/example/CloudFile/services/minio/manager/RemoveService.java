package com.example.CloudFile.services.minio.manager;

import com.example.CloudFile.services.minio.MinioService;
import com.example.CloudFile.util.UserPathProvider;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class RemoveService {

    private final String FOLDER_INDICATOR = "/";
    private final UserPathProvider pathProvider;
    private final MinioService minioService;

    public void remove(String path) {
        if (path.endsWith(FOLDER_INDICATOR)) {
            removeFolder(path);
        } else {
            minioService.removeObject(path);
        }
    }

    private void removeFolder(String path) {
        Iterable<Result<Item>> results = minioService.listFolderContent(path);
        Stream<Item> itemStream = StreamSupport
                .stream(results.spliterator(), false)
                .map(result -> {
                    try {
                        return result.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        itemStream.forEach(item -> {
            String fullObjectName = item.objectName();
            String relativeName = fullObjectName.substring(pathProvider.rootPath().length());
            try {
                minioService.removeObject(relativeName);
            } catch (Exception e ){
                log.warn("Не удалось удалить объект {}: {}", fullObjectName, e.getMessage());
            }
        });
    }
}
