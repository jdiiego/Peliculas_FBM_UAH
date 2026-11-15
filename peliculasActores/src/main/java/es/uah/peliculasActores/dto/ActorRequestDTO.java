package es.uah.peliculasActores.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ActorRequestDTO (
    @NotBlank(message = "El nombre del actor no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    String nombre,

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    LocalDate fechaNacimiento,

    @NotBlank(message = "El país de nacimiento del actor no puede estar vacío")
    @Size(max = 100, message = "El país de nacimiento no puede exceder los 100 caracteres")
    String paisNacimiento
) {

}


