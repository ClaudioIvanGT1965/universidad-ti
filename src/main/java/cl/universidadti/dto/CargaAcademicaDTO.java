package cl.universidadti.dto;

import java.math.BigDecimal;

/**
 * Vista resumida de una inscripción dentro de la carga académica.
 */
public class CargaAcademicaDTO {

    private Long idInscripcion;
    private String codigoCurso;
    private String nombreCurso;
    private Integer periodo;
    private String modulo;
    private BigDecimal promedio;

    public CargaAcademicaDTO() {
    }

    /**
     * Crea una vista de carga académica.
     *
     * @param idInscripcion identificador de la inscripción
     * @param codigoCurso código del curso
     * @param nombreCurso nombre del curso
     * @param periodo período académico
     * @param modulo módulo del curso
     * @param promedio promedio obtenido
     */
    public CargaAcademicaDTO(
            Long idInscripcion,
            String codigoCurso,
            String nombreCurso,
            Integer periodo,
            String modulo,
            BigDecimal promedio) {

        this.idInscripcion = idInscripcion;
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.periodo = periodo;
        this.modulo = modulo;
        this.promedio = promedio;
    }

    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }
}
