package es.uah.peliculasActores.repository;

import es.uah.peliculasActores.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    @Query("select distinct p from Pelicula p left join p.actores a" +
            " where (:titulo IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))" +
            " AND (:genero IS NULL OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%')))" +
            " AND (:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Pelicula> buscar(@Param("titulo") String titulo, @Param("genero") String genero, @Param("nombre") String nombre);
}
