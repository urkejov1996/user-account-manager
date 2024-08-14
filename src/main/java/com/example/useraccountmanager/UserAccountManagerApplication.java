package com.example.useraccountmanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class UserAccountManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAccountManagerApplication.class, args);
	}



	@Bean
	public WebMvcConfigurer webMvcConfigurer(Environment environment) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedHeaders("Access-Control-Allow-Origin",
								"*",
								"Access-Control-Allow-Methods",
								"POST, GET, OPTIONS, PUT, DELETE",
								"Access-Control-Allow-Headers",
								"Origin, X-Requested-With, Content-Type, Accept")
						.allowedOrigins("*")
						.allowedMethods("*");
			}
		};

	}
}
