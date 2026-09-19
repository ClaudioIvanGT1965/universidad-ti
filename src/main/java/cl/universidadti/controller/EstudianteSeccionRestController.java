package cl.universidadti.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.universidadti.dto.InscripcionDTO;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.service.InscripcionService;

@RestController
@RequestMapping("/api/inscripciones")
public class EstudianteSeccionRestController {

    private final EstudianteSeccionRepository repository;

    private final InscripcionService inscripcionService;

    public EstudianteSeccionRestController(
            EstudianteSeccionRepository repository,
            InscripcionService inscripcionService) {

        this.repository = repository;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public List<EstudianteSeccion> listar() {
        return repository.findAll();
    }

    @GetMapping("/estudiante/{id}")
    public List<EstudianteSeccion> cargaAcademica(
            @PathVariable Long id) {

        return repository.findByEstudianteId(id);
    }

    @GetMapping("/seccion/{id}")
    public List<EstudianteSeccion> estudiantesPorSeccion(
            @PathVariable Long id) {

        return repository.findBySeccionId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudianteSeccion inscribir(
            @RequestBody InscripcionDTO dto) {

        return inscripcionService.inscribir(dto);
    }

    @DeleteMapping("/{id}")
    public void retirar(
            @PathVariable Long id) {

        inscripcionService.retirar(id);
    }
}
