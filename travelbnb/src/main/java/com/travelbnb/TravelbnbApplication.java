package com.travelbnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class TravelbnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelbnbApplication.class, args);
	}

}
