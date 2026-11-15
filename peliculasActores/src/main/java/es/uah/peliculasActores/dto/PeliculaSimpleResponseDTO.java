package es.uah.peliculasActores.dto;

import es.uah.peliculasActores.model.Pelicula;

public record PeliculaSimpleResponseDTO (
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
    public static PeliculaSimpleResponseDTO fromEntity(Pelicula p) {
        if (p == null) {
            return null;
        }
        return new PeliculaSimpleResponseDTO(
                p.getIdPelicula(),
                p.getTitulo(),
                p.getAnio(),
                p.getDuracion(),
                p.getPais(),
                p.getDirector(),
                p.getGenero(),
                p.getSinopsis(),
                p.getImagen()
        );
    }
}
