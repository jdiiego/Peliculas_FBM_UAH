package es.uah.peliculasActores.repository;

import es.uah.peliculasActores.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    List<Pelicula> findByGeneroContainingIgnoreCase(String genero);

    List<Pelicula> findDistinctByActores_NombreContainingIgnoreCase(String nombre);
}
