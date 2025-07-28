package com.example.Utown.config.S3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {
    private String region;
    private String s3Bucket;
    private Credentials credentials;

    @Data
    public static class Credentials{
        private String accessKey;
        private String secretKey;
    }
}
