package cl.universidadti.dto;

/**
 * Curso vigente expuesto dentro del plan curricular.
 */
public class CursoPlanDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;

    public CursoPlanDTO() {
    }

    /**
     * Crea una vista de curso del plan curricular.
     *
     * @param id identificador del curso
     * @param codigo código del curso
     * @param nombre nombre del curso
     * @param descripcion descripción del curso
     */
    public CursoPlanDTO(
            Long id,
            String codigo,
            String nombre,
            String descripcion) {

        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
