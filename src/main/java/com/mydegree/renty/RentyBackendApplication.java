package com.mydegree.renty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class RentyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentyBackendApplication.class, args);
	}

}
