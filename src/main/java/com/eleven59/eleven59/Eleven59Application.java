package com.eleven59.eleven59;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class Eleven59Application {

	public static void main(String[] args) {
		SpringApplication.run(Eleven59Application.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("ELEVEN59 API")
						.version("1.0")
						.description("API documentation for the 11:59 project")
						.contact(new Contact()
								.name("Eleven59")
						));
	}

}
