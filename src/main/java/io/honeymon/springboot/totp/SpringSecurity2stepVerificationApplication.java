package io.honeymon.springboot.totp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@SpringBootApplication(exclude = { ThymeleafAutoConfiguration.class })
public class SpringSecurity2stepVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity2stepVerificationApplication.class, args);
	}
}
