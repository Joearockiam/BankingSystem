package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BsSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsSchedulerApplication.class, args);
	}

}
