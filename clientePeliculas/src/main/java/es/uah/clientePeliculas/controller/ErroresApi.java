package es.uah.clientePeliculas.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

final class ErroresApi {

    private record ErrorValidacion(String detail, Map<String, String> errors) {
    }

    private ErroresApi() {
    }

    static void volcar(HttpClientErrorException.BadRequest ex, BindingResult resultado) {
        ErrorValidacion error = ex.getResponseBodyAs(ErrorValidacion.class);
        if (error == null || error.errors() == null || error.errors().isEmpty()) {
            resultado.reject("api", error != null && error.detail() != null
                    ? error.detail() : "Los datos no son válidos.");
            return;
        }
        error.errors().forEach((campo, mensaje) -> {
            if (resultado.getFieldType(campo) != null) {
                resultado.rejectValue(campo, "api", mensaje);
            } else {
                resultado.reject("api", mensaje);
            }
        });
    }
}
