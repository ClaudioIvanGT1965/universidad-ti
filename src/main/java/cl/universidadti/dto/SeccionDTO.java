package cl.universidadti.dto;

import java.time.LocalDate;

public class SeccionDTO {

    private Integer periodo;
    private String codigoCurso;
    private Integer topeEstudiantes;
    private Integer totalNotas;
    private LocalDate fechaIni;
    private LocalDate fechaTer;
    private String modulo;

    public SeccionDTO() {
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
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
