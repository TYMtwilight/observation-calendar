package com.example.observationcalendar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secretKey = "your-secret-key-change-in-production";
    private long expirationMs = 864000000; // 24時間（ミリ秒）
    private long refreshExpirationMs = 604800000; // 7日間（ミリ秒）
    private String tokenPrefix = "Bearer";
    private String headerName = "Authorization";
}
