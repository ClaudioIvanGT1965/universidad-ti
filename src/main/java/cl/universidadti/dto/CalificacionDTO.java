package cl.universidadti.dto;

import java.math.BigDecimal;

/**
 * Calificación de un estudiante dentro de un lote de notas.
 */
public class CalificacionDTO {

    private Long idEstudianteSeccion;
    private BigDecimal nota;

    public CalificacionDTO() {
    }

    public Long getIdEstudianteSeccion() {
        return idEstudianteSeccion;
    }

    public void setIdEstudianteSeccion(Long idEstudianteSeccion) {
        this.idEstudianteSeccion = idEstudianteSeccion;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
}
