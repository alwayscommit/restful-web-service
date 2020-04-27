package com.mobilewebapp.ws.mobilewebappws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MobileWebappWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileWebappWsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringAppContext springAppContext() {
		return new SpringAppContext();
	}

}
