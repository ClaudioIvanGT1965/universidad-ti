package cl.universidadti.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "curso")
/** Entidad que representa un curso académico ofrecido por la universidad. */
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 180)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 2000)
    private String descripcion;

    @Column(name = "vigente", nullable = false)
    private Boolean vigente;

    /** Crea un curso vacío para uso de JPA. */
    public Curso() {
    }

    /** @return identificador persistente del curso */
    public Long getId() {
        return id;
    }

    /** @param id identificador persistente del curso */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return código único del curso */
    public String getCodigo() {
        return codigo;
    }

    /** @param codigo código único del curso */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /** @return nombre visible del curso */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nombre visible del curso */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return descripción del curso */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion descripción del curso */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return {@code true} si el curso está disponible */
    public Boolean getVigente() {
        return vigente;
    }

    /** @param vigente indica si el curso está disponible */
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }
}
