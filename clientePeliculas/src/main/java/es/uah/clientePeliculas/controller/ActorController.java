package es.uah.clientePeliculas.controller;

import es.uah.clientePeliculas.model.ActorForm;
import es.uah.clientePeliculas.service.ActorService;
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

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return mostrarFormulario(model, new ActorForm(), null);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        try {
            return mostrarFormulario(model, ActorForm.desde(actorService.buscarPorId(id)), id);
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "El actor no existe.");
            return "redirect:/actores";
        }
    }

    @PostMapping("/nuevo")
    public String crear(@ModelAttribute("actor") ActorForm form, BindingResult resultado,
                        Model model, RedirectAttributes redirect) {
        try {
            actorService.guardar(form);
            redirect.addFlashAttribute("msgExito", "Actor creado.");
            return "redirect:/actores";
        } catch (HttpClientErrorException.BadRequest e) {
            ErroresApi.volcar(e, resultado);
            return mostrarFormulario(model, form, null);
        }
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("actor") ActorForm form,
                             BindingResult resultado, Model model, RedirectAttributes redirect) {
        try {
            actorService.actualizar(id, form);
            redirect.addFlashAttribute("msgExito", "Actor actualizado.");
            return "redirect:/actores";
        } catch (HttpClientErrorException.BadRequest e) {
            ErroresApi.volcar(e, resultado);
            return mostrarFormulario(model, form, id);
        } catch (HttpClientErrorException.NotFound e) {
            redirect.addFlashAttribute("msgError", "El actor ya no existe.");
            return "redirect:/actores";
        }
    }

    private String mostrarFormulario(Model model, ActorForm form, Integer id) {
        model.addAttribute("actor", form);
        model.addAttribute("idActor", id);
        return "actores/form";
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
