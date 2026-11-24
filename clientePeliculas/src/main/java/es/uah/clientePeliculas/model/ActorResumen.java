package es.uah.clientePeliculas.model;

import java.time.LocalDate;

/** Actor tal como aparece dentro del reparto de una película (sin su lista de películas). */
public record ActorResumen(
        Integer idActor,
        String nombre,
        LocalDate fechaNacimiento,
        String paisNacimiento
) {
}
