package net.mapomi.mapomi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MapomiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapomiApplication.class, args);
	}

}
