package com.ubnarchival.archival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.File;

@SpringBootApplication
public class ArchivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivalApplication.class, args);
//		String password = BCrypt.hashpw("test1234", BCrypt.gensalt());
//		System.out.println(password);


		File dir = new File("C:\\Users\\ednwoji\\Documents");

		// Get the list of files in the directory
		File[] files = dir.listFiles();

		// Iterate through the files and find the folder that starts with the given name
		for (File file : files) {
			if (file.getName().startsWith("archival") || file.getName().endsWith(".zip")) {
				// This is the folder we're looking for
				System.out.println(file.getAbsolutePath());
				break;
			}
		}


	}

}
