package br.com.idus.chronos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChronosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChronosApplication.class, args);
	}

}
