package es.uah.peliculasActores.repository;

import es.uah.peliculasActores.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("select a from Actor a where (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))" +
            " AND (:paisNacimiento IS NULL OR LOWER(a.paisNacimiento) LIKE LOWER(CONCAT('%', :paisNacimiento, '%')))")
    List<Actor> buscar(@Param("nombre") String nombre, @Param("paisNacimiento") String paisNacimiento);
}
