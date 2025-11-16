package com.example.CloudFile.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ObjectDTO {
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long size;
    private String type;

    public ObjectDTO(String path, String name, Long size, String type) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.type = type;
    }

    public ObjectDTO(String path, String name, String type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public ObjectDTO(String path, String type) {
        this.path = path;
        this.type = type;
    }
}


