package es.uah.clientePeliculas.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ActorForm {

    private String nombre;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    private String paisNacimiento;

    public ActorForm() {
    }

    public static ActorForm desde(Actor actor) {
        ActorForm form = new ActorForm();
        form.nombre = actor.nombre();
        form.fechaNacimiento = actor.fechaNacimiento();
        form.paisNacimiento = actor.paisNacimiento();
        return form;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPaisNacimiento() {
        return paisNacimiento;
    }

    public void setPaisNacimiento(String paisNacimiento) {
        this.paisNacimiento = paisNacimiento;
    }
}
