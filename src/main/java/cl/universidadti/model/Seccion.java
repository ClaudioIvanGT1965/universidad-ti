package cl.universidadti.model;

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

/**
 * Entidad que representa una sección de un curso en un período académico.
 */
@Entity
@Table(name = "seccion")
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periodo", nullable = false)
    private Integer periodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "codigoCurso",
            referencedColumnName = "codigo",
            nullable = false
    )
    private Curso curso;

    @Column(name = "tope_estudiantes", nullable = false)
    private Integer topeEstudiantes;

    @Column(name = "total_notas", nullable = false)
    private Integer totalNotas;

    @Column(name = "fechaIni")
    private LocalDate fechaIni;

    @Column(name = "fechaTer")
    private LocalDate fechaTer;

    @Column(name = "modulo", length = 3)
    private String modulo;

    public Seccion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Integer getTopeEstudiantes() {
        return topeEstudiantes;
    }

    public void setTopeEstudiantes(Integer topeEstudiantes) {
        this.topeEstudiantes = topeEstudiantes;
    }

    public Integer getTotalNotas() {
        return totalNotas;
    }

    public void setTotalNotas(Integer totalNotas) {
        this.totalNotas = totalNotas;
    }

    public LocalDate getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(LocalDate fechaIni) {
        this.fechaIni = fechaIni;
    }

    public LocalDate getFechaTer() {
        return fechaTer;
    }

    public void setFechaTer(LocalDate fechaTer) {
        this.fechaTer = fechaTer;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
}
