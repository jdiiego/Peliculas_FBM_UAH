package es.uah.clientePeliculas.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErroresWeb {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String portadaDemasiadoGrande(RedirectAttributes redirect) {
        redirect.addFlashAttribute("msgError", "La portada supera el tamaño máximo de 5 MB.");
        return "redirect:/peliculas";
    }
}
