package es.uah.clientePeliculas.config;

import es.uah.clientePeliculas.service.PortadaService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sirve las portadas subidas en /uploads/** directamente desde la carpeta del disco,
 * sin necesidad de un método de controlador que lea el fichero.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PortadaService portadaService;

    public WebConfig(PortadaService portadaService) {
        this.portadaService = portadaService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(portadaService.getCarpeta().toUri().toString());
    }
}
