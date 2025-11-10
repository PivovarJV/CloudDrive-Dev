package com.example.CloudFile.services.minio.manager;

import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.services.minio.util.MinioService;
import com.example.CloudFile.util.MinioExecutor;
import com.example.CloudFile.util.UserPathProvider;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.CloudFile.validation.PathValidator.validate;

@Service
@RequiredArgsConstructor
public class CopyManager {

    private static final Logger log = LoggerFactory.getLogger(CopyManager.class);
    private final MinioService minioService;
    private final UserPathProvider pathProvider;
    private final MinioExecutor executor;

    public List<ObjectDTO> copy(String from, String to) {
        validate(from, to);
        List<ObjectDTO> list = new ArrayList<>();

        if (from.endsWith("/")) {
            log.info("Копирование папки из {} в {}", from, to);
            Iterable<Result<Item>> results = minioService.listFolderContent(from);
            for (Result<Item> result : results) {
                Item item = executor.execute(result::get);
                if (item.isDir()) continue;

                String fullName = item.objectName();
                String cleanFullName = fullName.substring(pathProvider.rootPath().length());
                String relative = cleanFullName.substring(from.length());
                String targetPath = to + relative;
                minioService.copyObject(cleanFullName, targetPath);
            }
        } else {
            log.info("Копирование файла из {} в {}", from, to);
            minioService.copyObject(from, to);
            list.add(minioService.statObject(to));
        }
        log.info("Копирование/переименование успешно выполнено в количестве: {}", list.size());
        return list;
    }
}
