package com.fyp.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

}
