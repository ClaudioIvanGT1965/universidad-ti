package cl.universidadti.dto;

import java.math.BigDecimal;
import java.util.List;

public class CursoNotasDTO {

    private String codigoCurso;
    private String nombreCurso;
    private Integer periodo;
    private String modulo;
    private BigDecimal promedio;
    private List<NotaDetalleDTO> notas;

    public CursoNotasDTO() {
    }

    public CursoNotasDTO(
            String codigoCurso,
            String nombreCurso,
            Integer periodo,
            String modulo,
            BigDecimal promedio,
            List<NotaDetalleDTO> notas) {

        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.periodo = periodo;
        this.modulo = modulo;
        this.promedio = promedio;
        this.notas = notas;
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

    public List<NotaDetalleDTO> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaDetalleDTO> notas) {
        this.notas = notas;
    }
}
