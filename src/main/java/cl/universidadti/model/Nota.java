package cl.universidadti.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_estudiante_seccion",
            nullable = false
    )
    private EstudianteSeccion estudianteSeccion;

    @Column(name = "numeroNota", nullable = false)
    private Integer numeroNota;

    @Column(name = "titulo", nullable = false, length = 180)
    private String titulo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "nota", nullable = false, precision = 4, scale = 2)
    private BigDecimal nota;

    @Column(name = "ponderacion", nullable = false)
    private Integer ponderacion;

    public Nota() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstudianteSeccion getEstudianteSeccion() {
        return estudianteSeccion;
    }

    public void setEstudianteSeccion(
            EstudianteSeccion estudianteSeccion) {
        this.estudianteSeccion = estudianteSeccion;
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
