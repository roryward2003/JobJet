package ie.tcd.scss.groupProject.config;

// This file is necessary for Swagger UI Implementation

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Group 17 APIs for CSU33012")
                        .version("1.0")
                        .description("API Documentation using SpringDoc OpenAPI")
                        .contact(new Contact()
                                .name("Rory Ward")
                                .email("wardr3@tcd.ie")))
                .addServersItem(new Server().url("http://localhost:8080")
                        .description("Local Development Server"));
    }
}