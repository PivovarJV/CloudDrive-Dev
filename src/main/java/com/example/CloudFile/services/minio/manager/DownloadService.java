package com.example.CloudFile.services.minio.manager;

import com.example.CloudFile.services.minio.MinioService;
import com.example.CloudFile.util.UserPathProvider;
import com.example.CloudFile.validation.PathValidator;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadService {

    private final MinioService minioService;
    private final UserPathProvider pathProvider;

    public ResponseEntity<StreamingResponseBody> downloadDirectory(String path) {
        PathValidator.validate(path);
        String trimmedPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        int lastSlash = trimmedPath.lastIndexOf("/");
        String archiveName = trimmedPath.substring(lastSlash + 1) + ".zip";

        StreamingResponseBody responseBody = outputStream -> {
            try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
                addDirectoryToZip(zipOut, path);
                zipOut.finish();
            } catch (Exception e) {
                log.warn("Ошибка при формировании архива");
                throw new RuntimeException("Ошибка при формировании архива", e);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(archiveName, StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(responseBody);
    }

    public ResponseEntity<StreamingResponseBody> downloadFile(String path) {
        log.info("Начало скачивание файла по пути: {}", pathProvider.rootPath() + path);
        PathValidator.validate(path);
        StreamingResponseBody responseBody = outputStream -> {
            InputStream inputStream = minioService.getObject(pathProvider.rootPath() + path);
            StreamUtils.copy(inputStream, outputStream);
        };
        String name = path.substring(path.lastIndexOf("/") + 1);
        ContentDisposition cd = ContentDisposition
                .attachment()
                .filename(name, StandardCharsets.UTF_8)
                .build();
        log.info("Скачивание файла успешно выполнено");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .body(responseBody);
    }

    private void addDirectoryToZip(ZipOutputStream zipOut, String path) throws Exception {
        Iterable<Result<Item>> results = minioService.listFolderContent(path);

        for (Result<Item> result : results) {
            Item item = result.get();
            String fullName = item.objectName();

            if (item.isDir()) {
                addDirectoryToZip(zipOut, fullName);
            } else {
                String relativeName = fullName.substring(pathProvider.rootPath().length());
                try (InputStream inputStream = minioService.getObject(fullName)) {
                    zipOut.putNextEntry(new ZipEntry(relativeName));
                    inputStream.transferTo(zipOut);
                    zipOut.closeEntry();
                }
            }
        }
    }
}
