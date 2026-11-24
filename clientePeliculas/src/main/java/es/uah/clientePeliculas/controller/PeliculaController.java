package es.uah.clientePeliculas.controller;

import es.uah.clientePeliculas.service.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
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

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            peliculaService.eliminar(id);
            redirect.addFlashAttribute("msgExito", "Pelicula eliminada.");
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "La pelicula ya no existe.");
        }
        return "redirect:/peliculas";
    }
}
