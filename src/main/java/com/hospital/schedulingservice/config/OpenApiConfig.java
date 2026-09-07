package com.hospital.schedulingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hospital Scheduling Service API")
                        .version("0.0.1-SNAPSHOT")
                        .description("API responsável pelo gerenciamento e agendamento de consultas/hospitalizações do Tech Challenge - Fase 3.")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("suporte@hospital.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
