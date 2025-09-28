package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.payment", "com.example.project1"})
public class PaymentApplication {

	public static void main(String[] args) {
		// Set active profile to payment
		System.setProperty("spring.profiles.active", "payment");
		System.setProperty("server.port", "8081");
		SpringApplication.run(PaymentApplication.class, args);
	}

}
