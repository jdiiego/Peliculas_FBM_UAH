package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Actor;

import java.util.List;

public interface ActorService {

    List<Actor> buscar(String nombre, String paisNacimiento);

    Actor buscarPorId(Integer id);

    void eliminar(Integer id);
}
