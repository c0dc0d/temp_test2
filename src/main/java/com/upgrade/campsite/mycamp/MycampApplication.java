package com.upgrade.campsite.mycamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class MycampApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycampApplication.class, args);
	}

}

