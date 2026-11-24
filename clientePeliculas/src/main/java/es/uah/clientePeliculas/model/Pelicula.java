package es.uah.clientePeliculas.model;

import java.util.List;

/** Película tal como la devuelve GET /api/peliculas y GET /api/peliculas/{id}, con su reparto. */
public record Pelicula(
        Integer idPelicula,
        String titulo,
        Integer anio,
        Integer duracion,
        String pais,
        String director,
        String genero,
        String sinopsis,
        String imagen,
        List<ActorResumen> actores
) {
}
