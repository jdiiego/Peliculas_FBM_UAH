package es.uah.clientePeliculas.model;

import java.time.LocalDate;
import java.util.List;

/** Actor tal como lo devuelve GET /api/actores y GET /api/actores/{id}. */
public record Actor(
        Integer idActor,
        String nombre,
        LocalDate fechaNacimiento,
        String paisNacimiento,
        List<PeliculaResumen> peliculas
) {
}
