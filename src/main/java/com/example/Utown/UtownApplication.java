package com.example.Utown;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.config.Utills.JWTProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({AwsProperties.class, JWTProperties.class})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class UtownApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtownApplication.class, args);
	}

}

