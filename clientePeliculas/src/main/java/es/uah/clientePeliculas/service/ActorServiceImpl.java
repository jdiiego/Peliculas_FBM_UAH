package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Actor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class ActorServiceImpl implements ActorService {

    private static final String RUTA = "/api/actores";

    private final RestClient restClient;

    public ActorServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<Actor> buscar(String nombre, String paisNacimiento) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(RUTA)
                        .queryParamIfPresent("nombre", Optional.ofNullable(nombre))
                        .queryParamIfPresent("paisNacimiento", Optional.ofNullable(paisNacimiento))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Actor>>() {});
    }

    @Override
    public Actor buscarPorId(Integer id) {
        return restClient.get()
                .uri(RUTA + "/{id}", id)
                .retrieve()
                .body(Actor.class);
    }

    @Override
    public void eliminar(Integer id) {
        restClient.delete()
                .uri(RUTA + "/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
