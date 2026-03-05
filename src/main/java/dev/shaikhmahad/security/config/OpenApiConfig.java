package dev.shaikhmahad.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Enterprise API Template")
                        .description("A reusable, industry-standard backend boilerplate designed for fast project bootstrapping.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shaikh Mahad")
                                .url("https://shaikhmahad.vercel.app")));
    }
}