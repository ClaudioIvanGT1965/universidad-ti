package cl.universidadti.dto;

import java.time.LocalDate;

public class SeccionDisponibleDTO {

    private Long idSeccion;
    private String codigoCurso;
    private String nombreCurso;
    private Integer periodo;
    private String modulo;
    private Integer cuposDisponibles;
    private LocalDate fechaIni;
    private LocalDate fechaTer;

    public SeccionDisponibleDTO() {
    }

    public SeccionDisponibleDTO(
            Long idSeccion,
            String codigoCurso,
            String nombreCurso,
            Integer periodo,
            String modulo,
            Integer cuposDisponibles,
            LocalDate fechaIni,
            LocalDate fechaTer) {

        this.idSeccion = idSeccion;
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.periodo = periodo;
        this.modulo = modulo;
        this.cuposDisponibles = cuposDisponibles;
        this.fechaIni = fechaIni;
        this.fechaTer = fechaTer;
    }

    public Long getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(Long idSeccion) {
        this.idSeccion = idSeccion;
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

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
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
}
