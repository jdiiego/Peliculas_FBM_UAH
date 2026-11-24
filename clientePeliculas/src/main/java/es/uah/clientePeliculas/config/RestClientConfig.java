package es.uah.clientePeliculas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP compartido para llamar al microservicio de películas y actores.
 * La URL base sale de application.properties para poder apuntar al Gateway en el trabajo final
 * sin tocar el código.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient peliculasApi(@Value("${peliculas.api.url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
