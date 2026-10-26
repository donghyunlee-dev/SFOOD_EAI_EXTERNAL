package com.sfood.eai.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SfoodEaiExternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SfoodEaiExternalApplication.class);
	}
}
