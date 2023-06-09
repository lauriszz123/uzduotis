package com.laurynas.uzduotis;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Uzduotis REST API")
                        .description("This API was created for a job application")
                        .version("1.0"))
                .addServersItem(new Server().url(getFileUrl("build/docs/")));
    }

    private String getFileUrl(String filePath) {
        return Paths.get(filePath).toUri().toString();
    }
}