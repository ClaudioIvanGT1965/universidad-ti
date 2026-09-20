package cl.universidadti.dto;

import java.time.LocalDateTime;

/**
 * Respuesta estándar para comunicar errores de la API.
 */
public class ApiError {

    private int estado;
    private String error;
    private LocalDateTime fecha;

    /**
     * Crea un error con su estado HTTP y mensaje.
     *
     * @param estado estado HTTP asociado
     * @param error descripción del error
     */
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
