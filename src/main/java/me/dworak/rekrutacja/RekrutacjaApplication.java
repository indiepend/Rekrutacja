package me.dworak.rekrutacja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class RekrutacjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RekrutacjaApplication.class, args);
	}

}
