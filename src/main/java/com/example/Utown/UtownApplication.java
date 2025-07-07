package com.example.Utown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UtownApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtownApplication.class, args);
	}

}
