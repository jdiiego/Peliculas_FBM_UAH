package es.uah.peliculasActores.controller;

import es.uah.peliculasActores.dto.ActorRequestDTO;
import es.uah.peliculasActores.dto.ActorResponseDTO;
import es.uah.peliculasActores.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/actores")
public class ActorController {
    private final ActorService actoresService;

    public ActorController(ActorService actoresService) {
        this.actoresService = actoresService;
    }

    @GetMapping
    public ResponseEntity<List<ActorResponseDTO>> buscar(@RequestParam(required = false) String nombre,
                                                              @RequestParam(required = false) String paisNacimiento) {
        return ResponseEntity.ok(actoresService.buscar(nombre, paisNacimiento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> buscarPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(actoresService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ActorResponseDTO> guardar(@Valid @RequestBody ActorRequestDTO request) {
        ActorResponseDTO nuevoActor = actoresService.guardar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoActor.idActor())
                .toUri();
        return ResponseEntity.created(location).body(nuevoActor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> actualizar(@PathVariable("id") Integer id, @Valid @RequestBody ActorRequestDTO request) {
        return ResponseEntity.ok(actoresService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        actoresService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
