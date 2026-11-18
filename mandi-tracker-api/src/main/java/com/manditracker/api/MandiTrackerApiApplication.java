package com.manditracker.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MandiTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MandiTrackerApiApplication.class, args);
	}

}
