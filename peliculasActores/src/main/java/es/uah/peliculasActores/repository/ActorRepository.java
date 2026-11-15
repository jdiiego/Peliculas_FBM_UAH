package es.uah.peliculasActores.repository;

import es.uah.peliculasActores.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByNombreContainingIgnoreCase(String nombre);

    List<Actor> findByPaisNacimientoContainingIgnoreCase(String paisNacimiento);
}
