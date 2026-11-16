package es.uah.peliculasActores.service;

import es.uah.peliculasActores.dto.PeliculaRequestDTO;
import es.uah.peliculasActores.dto.PeliculaResponseDTO;
import java.util.List;

public interface PeliculaService {
    PeliculaResponseDTO buscarPorId(Integer id);

    List<PeliculaResponseDTO> buscar(String titulo, String genero, String nombre);

    PeliculaResponseDTO guardar(PeliculaRequestDTO dto);

    PeliculaResponseDTO actualizar(Integer id, PeliculaRequestDTO dto);

    void eliminar(Integer id);
}
