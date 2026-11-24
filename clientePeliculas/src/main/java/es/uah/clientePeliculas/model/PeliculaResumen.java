package es.uah.clientePeliculas.model;

/**
 * Película tal como aparece dentro de la ficha de un actor (sin reparto).
 * Los nombres coinciden con el JSON del backend: así Jackson rellena cada campo solo.
 */
public record PeliculaResumen(
        Integer idPelicula,
        String titulo,
        Integer anio,
        Integer duracion,
        String pais,
        String director,
        String genero,
        String sinopsis,
        String imagen
) {
}
