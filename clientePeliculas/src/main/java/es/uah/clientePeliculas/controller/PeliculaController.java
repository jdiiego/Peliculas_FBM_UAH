package es.uah.clientePeliculas.controller;

import es.uah.clientePeliculas.model.Pelicula;
import es.uah.clientePeliculas.model.PeliculaForm;
import es.uah.clientePeliculas.service.ActorService;
import es.uah.clientePeliculas.service.PeliculaService;
import es.uah.clientePeliculas.service.PortadaService;
import es.uah.clientePeliculas.service.PortadaService.PortadaNoValidaException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    /** Géneros sugeridos en el formulario; se puede escribir otro. */
    private static final List<String> GENEROS = List.of(
            "Acción", "Animación", "Aventura", "Ciencia ficción", "Comedia", "Documental",
            "Drama", "Fantasía", "Musical", "Romance", "Suspense", "Terror", "Western");

    private final PeliculaService peliculaService;
    private final ActorService actorService;
    private final PortadaService portadaService;

    public PeliculaController(PeliculaService peliculaService, ActorService actorService,
                              PortadaService portadaService) {
        this.peliculaService = peliculaService;
        this.actorService = actorService;
        this.portadaService = portadaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String titulo,
                         @RequestParam(required = false) String genero,
                         @RequestParam(required = false) String actor,
                         Model model) {
        model.addAttribute("peliculas", peliculaService.buscar(titulo, genero, actor));
        model.addAttribute("titulo", titulo);
        model.addAttribute("genero", genero);
        model.addAttribute("actor", actor);
        return "peliculas/lista";
    }

    @GetMapping("/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("pelicula", peliculaService.buscarPorId(id));
            return "peliculas/ver";
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "La película no existe.");
            return "redirect:/peliculas";
        }
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        return mostrarFormulario(model, new PeliculaForm(), null);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        try {
            return mostrarFormulario(model, PeliculaForm.desde(peliculaService.buscarPorId(id)), id);
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "La película no existe.");
            return "redirect:/peliculas";
        }
    }

    @PostMapping("/nueva")
    public String crear(@ModelAttribute("pelicula") PeliculaForm form, BindingResult resultado,
                        @RequestParam("portada") MultipartFile portada,
                        Model model, RedirectAttributes redirect) {
        return guardar(null, form, resultado, portada, model, redirect);
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("pelicula") PeliculaForm form,
                             BindingResult resultado, @RequestParam("portada") MultipartFile portada,
                             Model model, RedirectAttributes redirect) {
        return guardar(id, form, resultado, portada, model, redirect);
    }

    /**
     * Crear y actualizar comparten el mismo flujo: guardar la portada nueva (si hay), enviar la película
     * al backend y, según el resultado, borrar la portada que sobra (la nueva si falló, la antigua si se
     * sustituyó). Así no quedan imágenes huérfanas en la carpeta uploads.
     */
    private String guardar(Integer id, PeliculaForm form, BindingResult resultado, MultipartFile portada,
                           Model model, RedirectAttributes redirect) {
        String portadaAnterior = form.getImagen();
        String portadaNueva;
        try {
            portadaNueva = portadaService.guardar(portada);
        } catch (PortadaNoValidaException e) {
            resultado.rejectValue("imagen", "portada", e.getMessage());
            return mostrarFormulario(model, form, id);
        }
        if (portadaNueva != null) {
            form.setImagen(portadaNueva);
        }

        try {
            if (id == null) {
                peliculaService.guardar(form);
                redirect.addFlashAttribute("msgExito", "Película creada.");
            } else {
                peliculaService.actualizar(id, form);
                redirect.addFlashAttribute("msgExito", "Película actualizada.");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            portadaService.eliminar(portadaNueva);
            form.setImagen(portadaAnterior);
            ErroresApi.volcar(e, resultado);
            return mostrarFormulario(model, form, id);
        } catch (HttpClientErrorException.NotFound e) {
            portadaService.eliminar(portadaNueva);
            form.setImagen(portadaAnterior);
            resultado.reject("api", "La película o alguno de los actores elegidos ya no existe.");
            return mostrarFormulario(model, form, id);
        }

        if (portadaNueva != null) {
            portadaService.eliminar(portadaAnterior);
        }
        return "redirect:/peliculas";
    }

    /** Un solo formulario para crear y editar: si hay id, se está editando. */
    private String mostrarFormulario(Model model, PeliculaForm form, Integer id) {
        model.addAttribute("pelicula", form);
        model.addAttribute("idPelicula", id);
        model.addAttribute("todosActores", actorService.buscar(null, null));
        model.addAttribute("generos", GENEROS);
        return "peliculas/form";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            Pelicula pelicula = peliculaService.buscarPorId(id);
            peliculaService.eliminar(id);
            portadaService.eliminar(pelicula.imagen());
            redirect.addFlashAttribute("msgExito", "Película eliminada.");
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "La película ya no existe.");
        }
        return "redirect:/peliculas";
    }
}
