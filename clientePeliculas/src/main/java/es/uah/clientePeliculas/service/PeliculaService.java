package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Pelicula;
import es.uah.clientePeliculas.model.PeliculaForm;

import java.util.List;

public interface PeliculaService {

    List<Pelicula> buscar(String titulo, String genero, String actor);

    Pelicula buscarPorId(Integer id);

    Pelicula guardar(PeliculaForm form);

    Pelicula actualizar(Integer id, PeliculaForm form);

    void eliminar(Integer id);
}
