package api.apiPaymentButton.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /*@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("API_KEY"))
                .components(new Components().addSecuritySchemes("API_KEY",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER) // El API key se envia en el header
                                .name("API_KEY")))   // Aqui debe ser 'apiKey' para Swagger, aunque uses otro nombre
                .addSecurityItem(new SecurityRequirement().addList("API_KEY"));
    }*/

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("API_KEY",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("API_KEY"))
                        .addSecuritySchemes("token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))) // Definimos el esquema de seguridad para el Bearer Token
                .addSecurityItem(new SecurityRequirement()
                        .addList("API_KEY") // Aplica para la apiKey
                        .addList("token")); // Aplica para el Bearer Token
    }

}
