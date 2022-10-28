package com.narko.ServitorFOS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServitorFosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServitorFosApplication.class, args);
	}

}
