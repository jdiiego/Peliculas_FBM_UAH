package es.uah.clientePeliculas.controller;

import es.uah.clientePeliculas.service.ActorService;
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
@RequestMapping("/actores")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String paisNacimiento,
                         Model model) {
        model.addAttribute("actores", actorService.buscar(nombre, paisNacimiento));
        model.addAttribute("nombre", nombre);
        model.addAttribute("paisNacimiento", paisNacimiento);
        return "actores/lista";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            actorService.eliminar(id);
            redirect.addFlashAttribute("msgExito", "Actor eliminado.");
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "El actor ya no existe.");
        }
        return "redirect:/actores";
    }
}
