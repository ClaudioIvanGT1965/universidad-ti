package cl.universidadti.dto;

public class InscripcionDTO {

    private Long idEstudiante;
    private Long idSeccion;

    public InscripcionDTO() {
    }

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Long getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Long idSeccion) {
        this.idSeccion = idSeccion;
    }
}
