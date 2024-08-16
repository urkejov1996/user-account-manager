package com.example.useraccountmanager;

import com.example.useraccountmanager.service.UserService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;

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


	@Bean
	CommandLineRunner commandLineRunner(UserService userService, Environment environment) {

		return args -> {
			try {
				FileInputStream firebaseAccount = null;
				firebaseAccount = new FileInputStream("src/main/resources/firebase-cred.json");
//				firebaseAccount = new FileInputStream("/home/server/Documents/projects/java/kladionice-kvote/src/main/resources/firebase-cred.json");
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(firebaseAccount))
						.build();
				FirebaseApp.initializeApp(options);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("ServerApplication :: commandLineRunner :: Firebase init failed");
				System.exit(99);
			}
			userService.initSuperAdminIfNotExist(UserRequest
					.builder()
					.firstName("Primary")
					.username("Primary Admin")
					.lastName("Admin")
					.email("urkejov1996@gmail.com")
					.password("urkejov1996@gmail.com")
					.status(UserStatusEnum.ACTIVE)
					.build());
			log.info("\uD83D\uDE80 EBA KLADIONICARI");
		};
	}
}
