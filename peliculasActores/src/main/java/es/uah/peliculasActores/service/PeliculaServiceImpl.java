package es.uah.peliculasActores.service;

import es.uah.peliculasActores.dto.PeliculaRequestDTO;
import es.uah.peliculasActores.dto.PeliculaResponseDTO;
import es.uah.peliculasActores.exception.ResourceNotFoundException;
import es.uah.peliculasActores.model.Actor;
import es.uah.peliculasActores.model.Pelicula;
import es.uah.peliculasActores.repository.ActorRepository;
import es.uah.peliculasActores.repository.PeliculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeliculaServiceImpl implements PeliculaService{

    private final PeliculaRepository peliculasRepository;
    private final ActorRepository actoresRepository;

    public PeliculaServiceImpl(PeliculaRepository peliculasRepository, ActorRepository actoresRepository) {
        this.peliculasRepository = peliculasRepository;
        this.actoresRepository = actoresRepository;
    }

    @Override
    public PeliculaResponseDTO buscarPorId(Integer id) {
        return peliculasRepository.findById(id)
                .map(PeliculaResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la pelicula con id: " + id));
    }

    @Override
    public List<PeliculaResponseDTO> buscar(String titulo, String genero, String nombre) {
        titulo = limpiarParametro(titulo);
        genero = limpiarParametro(genero);
        nombre = limpiarParametro(nombre);
        return peliculasRepository.buscar(titulo, genero, nombre).stream()
                .map(PeliculaResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public PeliculaResponseDTO guardar(PeliculaRequestDTO dto) {
        Pelicula pelicula = new Pelicula(dto);
        asignarReparto(pelicula, dto.actores());
        Pelicula guardado = peliculasRepository.save(pelicula);
        return PeliculaResponseDTO.fromEntity(guardado);
    }

    @Override
    @Transactional
    public PeliculaResponseDTO actualizar(Integer id, PeliculaRequestDTO dto) {
        Pelicula pelicula = peliculasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la pelicula con id: " + id));
        pelicula.actualizarDesdeDTO(dto);
        asignarReparto(pelicula, dto.actores());
        Pelicula actualizado = peliculasRepository.save(pelicula);
        return PeliculaResponseDTO.fromEntity(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Pelicula pelicula = peliculasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la pelicula con id: " + id));
        peliculasRepository.delete(pelicula);
    }

    private void asignarReparto(Pelicula pelicula, List<Integer> idsActores) {
        if (idsActores == null || idsActores.isEmpty()) {
            pelicula.setActores(new ArrayList<>());
            return;
        }

        List<Actor> actoresEncontrados = actoresRepository.findAllById(idsActores);

        if (actoresEncontrados.size() != idsActores.size()) {
            throw new ResourceNotFoundException("Uno o más IDs de actores proporcionados no existen en la base de datos.");
        }

        pelicula.setActores(actoresEncontrados);
    }

    private String limpiarParametro(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
