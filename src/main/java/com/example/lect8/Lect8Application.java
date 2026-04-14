package com.example.lect8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"Lect_B.week04", "Lect_B.week05", "Lect_B.week06", "Lect_B.week07"})
public class Lect8Application {

	public static void main(String[] args) {
		SpringApplication.run(Lect8Application.class, args);
	}

}
