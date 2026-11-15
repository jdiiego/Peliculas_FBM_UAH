package es.uah.peliculasActores.dto;

import jakarta.validation.constraints.*;

public record PeliculaRequestDTO (
        @NotBlank(message = "El título de la pelicula no puede estar vacío")
        @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
        String titulo,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 1888, message = "El año mínimo permitido es 1888 (invención del cine)")
        @Max(value = 2100, message = "El año no puede ser superior a 2100")
        Integer anio,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración en minutos debe ser mayor a cero")
        @Max(value = 600, message = "La duración máxima no puede superar las 10 horas")
        Integer duracion,

        @NotBlank(message = "El país no puede estar vacío")
        @Size(max = 100, message = "El país no puede exceder los 100 caracteres")
        String pais,

        @NotBlank(message = "El director no puede estar vacío")
        @Size(max = 100, message = "El director no puede exceder los 100 caracteres")
        String director,

        @NotBlank(message = "El género no puede estar vacío")
        @Size(max = 50, message = "El género no puede exceder los 50 caracteres")
        String genero,

        @NotBlank(message = "La sinopsis no puede estar vacío")
        @Size(max = 2000, message = "La sinopsis no puede exceder los 2000 caracteres")
        String sinopsis,

        @Size(max = 250, message = "La imagne no puede exceder los 250 caracteres")
        String imagen
) {
}
