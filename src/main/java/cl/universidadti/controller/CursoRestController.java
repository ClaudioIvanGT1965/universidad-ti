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

import cl.universidadti.model.Curso;
import cl.universidadti.repository.CursoRepository;
import cl.universidadti.service.CursoService;

/**
 * Controlador REST para la gestión de cursos
 */
@RestController
@RequestMapping("/api/cursos")
public class CursoRestController {

    private final CursoRepository cursoRepository;
    private final CursoService cursoService;

    public CursoRestController(
            CursoRepository cursoRepository,
            CursoService cursoService) {

        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Curso crear(
            @RequestBody Curso curso) {

        return cursoService.crear(curso);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        cursoService.eliminarLogicamente(id);
    }

    @PutMapping("/{id}")
    public Curso actualizar(
            @PathVariable Long id,
            @RequestBody Curso curso) {

        return cursoService.actualizar(id, curso);
    }
}
