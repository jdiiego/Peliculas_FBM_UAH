package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Pelicula;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaServiceImpl implements PeliculaService {

    private static final String RUTA = "/api/peliculas";

    private final RestClient restClient;

    public PeliculaServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<Pelicula> buscar(String titulo, String genero, String actor) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(RUTA)
                        .queryParamIfPresent("titulo", Optional.ofNullable(titulo))
                        .queryParamIfPresent("genero", Optional.ofNullable(genero))
                        .queryParamIfPresent("actor", Optional.ofNullable(actor))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Pelicula>>() {});
    }

    @Override
    public Pelicula buscarPorId(Integer id) {
        return restClient.get()
                .uri(RUTA + "/{id}", id)
                .retrieve()
                .body(Pelicula.class);
    }

    @Override
    public void eliminar(Integer id) {
        restClient.delete()
                .uri(RUTA + "/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
