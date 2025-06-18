package com.example.Utown.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;
}

