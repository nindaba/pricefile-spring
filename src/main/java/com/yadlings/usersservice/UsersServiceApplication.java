package com.yadlings.usersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ImportResource({
		"classpath:userservice-security.xml",
		"classpath:userservice-spring.xml"
})
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class UsersServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}
}
