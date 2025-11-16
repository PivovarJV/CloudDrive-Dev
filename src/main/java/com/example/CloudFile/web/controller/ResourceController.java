package com.example.CloudFile.web.controller;

import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.services.minio.MinioService;
import com.example.CloudFile.services.minio.manager.CopyService;
import com.example.CloudFile.services.minio.manager.DownloadService;
import com.example.CloudFile.services.minio.manager.RemoveService;
import com.example.CloudFile.services.minio.manager.UploadService;
import com.example.CloudFile.validation.annotation.ValidPath;
import com.example.CloudFile.web.controller.api.ResourceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RequiredArgsConstructor
@RestController()
public class ResourceController implements ResourceApi {

    private final String FOLDER_INDICATOR = "/";
    private final MinioService minioService;
    private final DownloadService downloadManager;
    private final UploadService uploadManager;
    private final CopyService copyManager;
    private final RemoveService removeService;

    @Override
    public void removeObject(String path) {
        removeService.remove(path);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> downloadObject(String path) {
        return path.endsWith(FOLDER_INDICATOR)
                ? downloadManager.downloadDirectory(path)
                : downloadManager.downloadFile(path);
    }

    @Override
    public List<ObjectDTO> listObject(String path) {
        return minioService.listObjects(path);
    }

    @Override
    public List<ObjectDTO> getObject(String path) {
        return minioService.listObjects(path);
    }

    @Override
    public List<ObjectDTO> putObject(String path, MultipartFile[] object) {
        return uploadManager.putObject(path, object);
    }

    @Override
    public ObjectDTO createFolder(String path) {
        return minioService.createFolder(path);
    }

    @Override
    public List<ObjectDTO> search(String query) {
        return minioService.search(query);
    }

    @Override
    public List<ObjectDTO> copyObject(String from, String to) {
        return copyManager.copy(from, to);
    }
}