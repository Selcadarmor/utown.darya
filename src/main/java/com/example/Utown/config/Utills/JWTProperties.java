package com.example.Utown.config.Utills;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    private String accessSecret;
    private String refreshSecret;
    private long accessExpirationMs;
    private long refreshExpirationMs;
}



