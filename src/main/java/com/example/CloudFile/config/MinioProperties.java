package com.example.CloudFile.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
@Getter
public class MinioProperties {
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;

    public MinioProperties(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
}
