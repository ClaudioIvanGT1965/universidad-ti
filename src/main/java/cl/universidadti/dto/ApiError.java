package cl.universidadti.dto;

import java.time.LocalDateTime;

public class ApiError {

    private int estado;
    private String error;
    private LocalDateTime fecha;

    public ApiError(int estado, String error) {
        this.estado = estado;
        this.error = error;
        this.fecha = LocalDateTime.now();
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
