package com.ubnarchival.archival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class ArchivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivalApplication.class, args);
//		String password = BCrypt.hasw3hfgvvhpw("test1234", BCrypt.gensalt());
//		System.out.println(password);


	}

}
