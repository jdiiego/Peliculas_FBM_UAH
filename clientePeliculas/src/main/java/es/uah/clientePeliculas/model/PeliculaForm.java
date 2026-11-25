package es.uah.clientePeliculas.model;

import java.util.ArrayList;
import java.util.List;

public class PeliculaForm {

    private String titulo;
    private Integer anio;
    private Integer duracion;
    private String pais;
    private String director;
    private String genero;
    private String sinopsis;
    private String imagen;
    private List<Integer> actores = new ArrayList<>();

    public PeliculaForm() {
    }

    public static PeliculaForm desde(Pelicula p) {
        PeliculaForm form = new PeliculaForm();
        form.titulo = p.titulo();
        form.anio = p.anio();
        form.duracion = p.duracion();
        form.pais = p.pais();
        form.director = p.director();
        form.genero = p.genero();
        form.sinopsis = p.sinopsis();
        form.imagen = p.imagen();
        form.actores = p.actores().stream().map(ActorResumen::idActor).toList();
        return form;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Integer> getActores() {
        return actores;
    }

    public void setActores(List<Integer> actores) {
        this.actores = actores != null ? actores : new ArrayList<>();
    }
}
