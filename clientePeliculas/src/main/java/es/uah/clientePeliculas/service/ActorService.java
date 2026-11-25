package es.uah.clientePeliculas.service;

import es.uah.clientePeliculas.model.Actor;
import es.uah.clientePeliculas.model.ActorForm;

import java.util.List;

public interface ActorService {

    List<Actor> buscar(String nombre, String paisNacimiento);

    Actor buscarPorId(Integer id);

    Actor guardar(ActorForm form);

    Actor actualizar(Integer id, ActorForm form);

    void eliminar(Integer id);
}
