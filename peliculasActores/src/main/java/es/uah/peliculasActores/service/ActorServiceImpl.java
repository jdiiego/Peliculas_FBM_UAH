package es.uah.peliculasActores.service;

import es.uah.peliculasActores.dto.ActorRequestDTO;
import es.uah.peliculasActores.dto.ActorResponseDTO;
import es.uah.peliculasActores.exception.ResourceNotFoundException;
import es.uah.peliculasActores.model.Actor;
import es.uah.peliculasActores.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActorServiceImpl implements ActorService{

    private final ActorRepository actoresRepository;

    public ActorServiceImpl(ActorRepository actoresRepository) {
        this.actoresRepository = actoresRepository;
    }

    @Override
    public ActorResponseDTO buscarPorId(Integer id) {
        return actoresRepository.findById(id)
                .map(ActorResponseDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el actor con id: " + id));
    }

    @Override
    public List<ActorResponseDTO> buscar(String nombre, String paisNacimiento) {
        nombre = limpiarParametro(nombre);
        paisNacimiento = limpiarParametro(paisNacimiento);
        return actoresRepository.buscar(nombre, paisNacimiento).stream()
                .map(ActorResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ActorResponseDTO guardar(ActorRequestDTO dto) {
        Actor actor = new Actor(dto);
        Actor guardado = actoresRepository.save(actor);
        return ActorResponseDTO.fromEntity(guardado);
    }

    @Override
    @Transactional
    public ActorResponseDTO actualizar(Integer id, ActorRequestDTO dto) {
        Actor actor = actoresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el actor con id: " + id));
        actor.actualizarDesdeDTO(dto);
        Actor actualizado = actoresRepository.save(actor);
        return ActorResponseDTO.fromEntity(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Actor actor = actoresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el actor con id: " + id));
        actoresRepository.delete(actor);
    }

    private String limpiarParametro(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
