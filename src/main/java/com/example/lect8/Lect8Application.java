package com.example.lect8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.lect8", "Lect_B_week02"})
public class Lect8Application {

	public static void main(String[] args) {
		SpringApplication.run(Lect8Application.class, args);
	}

}
