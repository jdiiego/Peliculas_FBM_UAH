package es.uah.peliculasActores.dto;

import es.uah.peliculasActores.model.Actor;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record ActorResponseDTO (
        Integer idActor,
        String nombre,
        LocalDate fechaNacimiento,
        String paisNacimiento,
        List<PeliculaSimpleResponseDTO> peliculas
) {
    public static ActorResponseDTO fromEntity(Actor a) {
        if (a == null) {
            return null;
        }
        List<PeliculaSimpleResponseDTO> listaPeliculas = a.getPeliculas() != null
                ? a.getPeliculas().stream()
                .map(PeliculaSimpleResponseDTO::fromEntity)
                .toList()
                : Collections.emptyList();

        return new ActorResponseDTO(
                a.getIdActor(),
                a.getNombre(),
                a.getFechaNacimiento(),
                a.getPaisNacimiento(),
                listaPeliculas
        );
    }
}
