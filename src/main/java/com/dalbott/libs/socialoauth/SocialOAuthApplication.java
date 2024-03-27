package com.dalbott.libs.socialoauth;

import com.dalbott.libs.socialoauth.config.ApplicationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class SocialOAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialOAuthApplication.class, args);
	}



}
