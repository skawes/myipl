package com.myipl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyiplApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyiplApplication.class, args);
	}

}
