package com.example.CloudFile.web.controller.api;

import com.example.CloudFile.dto.ObjectDTO;
import com.example.CloudFile.validation.annotation.ValidPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RequestMapping("/api/v1")
@Tag(name = "Resource API", description = "Операции с файлами и папками")
public interface ResourceApi {

    @Operation(
            summary = "Удалить файл",
            description = "Удаление файла по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Файл успешно удален"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @DeleteMapping("/resource")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeObject(
            @Parameter(
                    description = "Путь, по которому лежит файл, который нужно удалить",
                    example = "folder/text.txt")
            @Valid @ValidPath@RequestParam String path);

    @Operation(
            summary = "Скачать файл или папку",
            description = "Скачивание файла или папки по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл или папка успешно скачен"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/resource/download")
    ResponseEntity<StreamingResponseBody> downloadObject(
            @Parameter(
                    description = "Путь, по которому лежит файл или папка, которую нужно скачать",
                    example = "folder/text.txt")
            @Valid @ValidPath @RequestParam String path);

    @Operation(
            summary = "Получить информацию о содержимом папки",
            description = "Получение информации о содержимом папки по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о содержимом папки получена"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Папка не существует"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/directory")
    List<ObjectDTO> listObject(
            @Parameter(
                    description = "Путь, по которому лежит папка, содержимое которой нужно предоставить",
                    example = "folder/")
            @Valid @ValidPath @RequestParam String path);

    @Operation(
            summary = "Получить информацию о ресурсе",
            description = "Получение информации по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о содержимом получена"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/resource")
    List<ObjectDTO> getObject(
            @Parameter(
                    description = "Путь объекта которой нужно предоставить",
                    example = "folder/text.txt")
            @Valid @ValidPath @RequestParam String path);


    @Operation(
            summary = "Загрузить файл или папку",
            description = "Загрузка файла или папки по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл или папка загружены"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/resource")
    List<ObjectDTO> putObject(
            @Parameter(
                    description = "Путь куда нужно сохранить файл или папку",
                    example = "folder/")
            @Valid @ValidPath @RequestParam String path,
            @Parameter(
                    description = "Файл или папка",
                    example = "folder/text.txt")
            @RequestParam MultipartFile[] object);

    @Operation(
            summary = "Создать пустую папку",
            description = "Создание пустой папки по указанной директории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пустая папка создана"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь к новой папки"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Родительская папка не существует"),
            @ApiResponse(responseCode = "409", description = "Папка уже существует"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @PostMapping("/directory")
    @ResponseStatus(HttpStatus.CREATED)
    ObjectDTO createFolder(
            @Parameter(
                    description = "Путь по которому нужно создать папку",
                    example = "folder/newFolder/")
            @Valid @ValidPath @RequestParam String path);

    @Operation(
            summary = "Поиск",
            description = "Поиска файла или папки по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Поиск выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий поисковый запрос"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/resource/search")
    List<ObjectDTO> search(
            @Parameter(
                    description = "Поисковый запрос (Что ищешь?)",
                    example = "Text")
            @Valid @ValidPath @RequestParam String query);

    @Operation(
            summary = "Переименование/перемещение ресурса",
            description = "Переименование/перемещение ресурса из указанной директории в новую директорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Переименование/перемещение выполнено"),
            @ApiResponse(responseCode = "400", description = "Невалидный или отсутствующий путь"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Ресурс не найден"),
            @ApiResponse(responseCode = "409", description = "Ресурс, лежащий по пути переноса уже существует"),
            @ApiResponse(responseCode = "500", description = "Неизвестная ошибка")
    })
    @GetMapping("/resource/move")
    List<ObjectDTO> copyObject(
            @Parameter(
                    description = "Начальный путь объекта (От куда хочешь перенести)",
                    example = "rootFolder/Folder/text.txt")
            @Valid @ValidPath @RequestParam String from,
            @Parameter(
                    description = "Конечный путь объекта (Куда хочешь перенести)",
                    example = "rootFolder/text.txt"
            )
            @Valid @ValidPath @RequestParam String to);
}