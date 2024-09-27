package com.jinwuui.localtravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LocalTravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalTravelApplication.class, args);
	}

}
