package br.com.sanittas.app.api.configuration.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração personalizada para o SpringDoc OpenAPI.
 */
@Configuration
public class SpringDocConfiguration {

    /**
     * Cria uma instância personalizada do OpenAPI para configurar esquemas de segurança.
     *
     * @return Uma instância personalizada do OpenAPI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
