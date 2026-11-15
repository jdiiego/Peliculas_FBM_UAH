package es.uah.peliculasActores.dto;

import es.uah.peliculasActores.model.Actor;
import java.time.LocalDate;

public record ActorSimpleResponseDTO (
    Integer idActor,
    String nombre,
    LocalDate fechaNacimiento,
    String paisNacimiento
) {
    public static ActorSimpleResponseDTO fromEntity(Actor a) {
        if (a == null) {
            return null;
        }
        return new ActorSimpleResponseDTO(
                a.getIdActor(),
                a.getNombre(),
                a.getFechaNacimiento(),
                a.getPaisNacimiento()
        );
    }
}
