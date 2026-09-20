package cl.universidadti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Universidad TI.
 */
@SpringBootApplication
public class UniversidadTiApplication {

    /**
     * Inicia el contexto de Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(UniversidadTiApplication.class, args);
    }

}
