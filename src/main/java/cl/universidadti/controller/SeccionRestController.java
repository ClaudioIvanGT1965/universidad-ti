package cl.universidadti.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.universidadti.dto.SeccionDTO;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.SeccionRepository;
import cl.universidadti.service.SeccionService;

/**
 * Controlador REST para administrar secciones de cursos.
 */
@RestController
@RequestMapping("/api/secciones")
public class SeccionRestController {

    private final SeccionRepository seccionRepository;
    private final SeccionService seccionService;

    public SeccionRestController(
            SeccionRepository seccionRepository,
            SeccionService seccionService) {

        this.seccionRepository = seccionRepository;
        this.seccionService = seccionService;
    }

    /**
     * @return todas las secciones registradas
     */
    @GetMapping
    public List<Seccion> listar() {

        return seccionRepository.findAll();
    }

    /**
     * Crea una sección a partir de sus datos de entrada.
     *
     * @param dto datos de la sección
     * @return sección persistida
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Seccion crear(
            @RequestBody SeccionDTO dto) {

        return seccionService.crear(dto);
    }

    /**
     * @param id identificador de la sección que se desea eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        seccionService.eliminar(id);
    }

    /**
     * @param id identificador de la sección
     * @param seccion nuevos datos de la sección
     * @return sección actualizada
     */
    @PutMapping("/{id}")
    public Seccion actualizar(
            @PathVariable Long id,
            @RequestBody SeccionDTO seccion) {

        return seccionService
                .actualizar(id, seccion);
    }
}
