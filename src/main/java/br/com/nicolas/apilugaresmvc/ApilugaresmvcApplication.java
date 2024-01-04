package br.com.nicolas.apilugaresmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ApilugaresmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApilugaresmvcApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("Nicolas0708!2712"));
	}

}
