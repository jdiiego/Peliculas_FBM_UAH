package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Pelicula;

import java.util.List;

public interface PeliculaService {

    List<Pelicula> buscar(String titulo, String genero, String actor);

    Pelicula buscarPorId(Integer id);

    void eliminar(Integer id);
}
