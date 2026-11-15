package es.uah.peliculasActores.dto;

import es.uah.peliculasActores.model.Pelicula;

import java.util.Collections;
import java.util.List;

public record PeliculaResponseDTO (
        Integer idPelicula,
        String titulo,
        Integer anio,
        Integer duracion,
        String pais,
        String director,
        String genero,
        String sinopsis,
        String imagen,
        List<ActorSimpleResponseDTO> actores
) {
    public static PeliculaResponseDTO fromEntity(Pelicula p) {
        if (p == null) {
            return null;
        }
        List<ActorSimpleResponseDTO> listaActores = p.getActores() != null
                ? p.getActores().stream()
                .map(ActorSimpleResponseDTO::fromEntity)
                .toList()
                : Collections.emptyList();

        return new PeliculaResponseDTO(
                p.getIdPelicula(),
                p.getTitulo(),
                p.getAnio(),
                p.getDuracion(),
                p.getPais(),
                p.getDirector(),
                p.getGenero(),
                p.getSinopsis(),
                p.getImagen(),
                listaActores
        );
    }
}
