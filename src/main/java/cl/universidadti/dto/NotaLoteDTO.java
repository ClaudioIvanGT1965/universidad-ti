package cl.universidadti.dto;

import java.time.LocalDate;
import java.util.List;

public class NotaLoteDTO {

    private Integer numeroNota;
    private String titulo;
    private LocalDate fecha;
    private Integer ponderacion;

    private List<CalificacionDTO> calificaciones;

    public NotaLoteDTO() {
    }

    public Integer getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Integer numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(Integer ponderacion) {
        this.ponderacion = ponderacion;
    }

    public List<CalificacionDTO> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(
            List<CalificacionDTO> calificaciones) {

        this.calificaciones = calificaciones;
    }
}
