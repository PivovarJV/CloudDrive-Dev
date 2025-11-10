package com.example.CloudFile.mapper;

import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.util.MinioExecutor;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ObjectMapper {

    private final MinioExecutor executor;

    public List<ObjectDTO> mapResultsToDTOs(Iterable<Result<Item>> results, String folder, String rootPath) {
        List<ObjectDTO> list = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = executor.execute(result::get);
            String objectName = item.objectName();

            if (objectName.equals(rootPath)) {
                continue;
            }

            if (objectName.startsWith(rootPath)) {
                objectName = objectName.substring(rootPath.length());
            }

            if (objectName.equals(folder) || objectName.equals(folder + "/")) {
                continue;
            }

            String name;
            String path;
            String type;

            if (objectName.endsWith("/")) {
                type = "DIRECTORY";
                String trimmedPath = objectName.substring(0,objectName.length() - 1);
                int lastSlash = trimmedPath.lastIndexOf("/");
                if (lastSlash == -1) {
                    path = "";
                    name = objectName;
                } else {
                    path = trimmedPath.substring(0, lastSlash + 1);
                    name = trimmedPath.substring(lastSlash + 1);
                    name = name + "/";
                }
                list.add(new ObjectDTO(path, name, type));
            } else {
                type = "FILE";
                int lastSlash = objectName.lastIndexOf("/");
                if (lastSlash == -1) {
                    path = "";
                    name = objectName;
                } else {
                    path = objectName.substring(0, lastSlash + 1);
                    name = objectName.substring(lastSlash + 1);
                }
                list.add(new ObjectDTO(path, name, item.size(), type));
            }
        }
        return list;
    }
}
