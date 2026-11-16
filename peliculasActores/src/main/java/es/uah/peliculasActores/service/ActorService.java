package es.uah.peliculasActores.service;

import es.uah.peliculasActores.dto.ActorRequestDTO;
import es.uah.peliculasActores.dto.ActorResponseDTO;
import java.util.List;

public interface ActorService {
    List<ActorResponseDTO> buscarTodos();

    ActorResponseDTO buscarPorId(Integer id);

    List<ActorResponseDTO> buscar(String nombre, String paisNacimiento);

    ActorResponseDTO guardar(ActorRequestDTO dto);

    ActorResponseDTO actualizar(Integer id, ActorRequestDTO dto);

    void eliminar(Integer id);
}
