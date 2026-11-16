package es.uah.peliculasActores.model;

import es.uah.peliculasActores.dto.ActorRequestDTO;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "actores")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idActor;

    private String nombre;

    private String paisNacimiento;

    private LocalDate fechaNacimiento;

    @ManyToMany(mappedBy = "actores")
    private List<Pelicula> peliculas = new ArrayList<>();

    public Actor(){
    }

    public Actor(ActorRequestDTO dto) {
        actualizarDesdeDTO(dto);
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas != null ? peliculas : new ArrayList<>();
    }

    public void actualizarDesdeDTO(ActorRequestDTO dto) {
        if (dto != null) {
            this.nombre = dto.nombre();
            this.paisNacimiento = dto.paisNacimiento();
            this.fechaNacimiento = dto.fechaNacimiento();
        }
    }

    public Integer getIdActor() {
        return idActor;
    }

    public void setIdActor(Integer idActor) {
        this.idActor = idActor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaisNacimiento() {
        return paisNacimiento;
    }

    public void setPaisNacimiento(String paisNacimiento) {
        this.paisNacimiento = paisNacimiento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return idActor != null && Objects.equals(idActor, actor.idActor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idActor);
    }

}
