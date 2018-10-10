package com.book.bookrating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@AutoConfigurationPackage
public class BookratingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookratingApplication.class, args);
	}
}
