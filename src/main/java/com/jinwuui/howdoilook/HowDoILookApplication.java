package com.jinwuui.howdoilook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HowDoILookApplication {

	public static void main(String[] args) {
		SpringApplication.run(HowDoILookApplication.class, args);
	}

}
