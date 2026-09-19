package cl.universidadti.dto;

public class CursoPlanDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;

    public CursoPlanDTO() {
    }

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
