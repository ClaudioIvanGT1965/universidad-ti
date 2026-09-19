package cl.universidadti.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NotaDetalleDTO {

    private Integer numeroNota;
    private String titulo;
    private LocalDate fecha;
    private BigDecimal nota;
    private Integer ponderacion;

    public NotaDetalleDTO() {
    }

    public NotaDetalleDTO(
            Integer numeroNota,
            String titulo,
            LocalDate fecha,
            BigDecimal nota,
            Integer ponderacion) {

        this.numeroNota = numeroNota;
        this.titulo = titulo;
        this.fecha = fecha;
        this.nota = nota;
        this.ponderacion = ponderacion;
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

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public Integer getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(Integer ponderacion) {
        this.ponderacion = ponderacion;
    }
}
