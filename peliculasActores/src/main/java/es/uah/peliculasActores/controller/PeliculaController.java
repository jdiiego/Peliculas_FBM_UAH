package es.uah.peliculasActores.controller;

import es.uah.peliculasActores.dto.ActorRequestDTO;
import es.uah.peliculasActores.dto.ActorResponseDTO;
import es.uah.peliculasActores.dto.PeliculaResponseDTO;
import es.uah.peliculasActores.dto.PeliculaRequestDTO;
import es.uah.peliculasActores.service.ActorService;
import es.uah.peliculasActores.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {
    private final PeliculaService peliculasService;

    public PeliculaController(PeliculaService peliculasService) {
        this.peliculasService = peliculasService;
    }

    @GetMapping
    public ResponseEntity<List<PeliculaResponseDTO>> buscar(@RequestParam(required = false) String titulo,
                                                            @RequestParam(required = false) String genero,
                                                            @RequestParam(required = false) String actor) {
        return ResponseEntity.ok(peliculasService.buscar(titulo, genero, actor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> buscarPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(peliculasService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PeliculaResponseDTO> guardar(@Valid @RequestBody PeliculaRequestDTO request) {
        PeliculaResponseDTO nuevaPelicula = peliculasService.guardar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaPelicula.idPelicula())
                .toUri();
        return ResponseEntity.created(location).body(nuevaPelicula);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> actualizar(@PathVariable("id") Integer id, @Valid @RequestBody PeliculaRequestDTO request) {
        return ResponseEntity.ok(peliculasService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        peliculasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
