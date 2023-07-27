package com.hritvik.BloggingPlatformAPI;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Blogging Platform API",
				description = "This a project of Blogging Platform API for Geekster MCT ",
				version = "v1.0.1",
				contact = @Contact(
						name = "Hritvik Soni",
						email = "hritviksoni768@gmail.com",
						url="https://www.linkedin.com/in/hritvik-soni/"
				),
				license = @License(
						name="Blogging Platform API License",
						url = "https://github.com/hritvik-soni"
				)
		),
		externalDocs = @ExternalDocumentation(
				description="External Docs",
				url="https://github.com/hritvik-soni"
		)
)
public class BloggingPlatformApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggingPlatformApiApplication.class, args);
	}

}
