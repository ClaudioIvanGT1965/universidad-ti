package cl.universidadti.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.universidadti.dto.ApiError;

/**
 * Convierte excepciones de validación en respuestas HTTP uniformes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de argumentos producidos por las reglas de negocio.
     *
     * @param ex excepción que contiene el detalle para el cliente
     * @return respuesta con estado HTTP 400 y cuerpo {@link ApiError}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarIllegalArgument(
            IllegalArgumentException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
