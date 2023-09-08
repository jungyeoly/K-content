package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(KContentApplication.class, args);

	}

}
