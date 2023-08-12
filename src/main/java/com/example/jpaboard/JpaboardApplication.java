package com.example.jpaboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(
		auditorAwareRef = "auditorAwareConfig",
		dateTimeProviderRef = "auditorAwareConfig"
)
public class JpaboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaboardApplication.class, args);
	}

}
