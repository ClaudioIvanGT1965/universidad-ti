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

    /**
        * Crea el controlador con sus dependencias de persistencia y negocio.
        *
        * @param cursoRepository repositorio utilizado para consultar cursos
        * @param cursoService servicio que aplica las reglas de negocio de cursos
     */
    public CursoRestController(
            CursoRepository cursoRepository,
            CursoService cursoService) {

        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
    }

    /**
     * Lista todos los cursos almacenados.
     *
     * @return cursos registrados
     */
    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    /**
     * Crea un curso nuevo y lo marca como vigente mediante el servicio.
     *
     * @param curso datos del curso que se desea crear
     * @return curso persistido
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Curso crear(
            @RequestBody Curso curso) {

        return cursoService.crear(curso);
    }

    /**
     * Realiza la eliminación lógica de un curso.
     *
     * @param id identificador del curso
     */
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        cursoService.eliminarLogicamente(id);
    }

    /**
     * Actualiza los datos editables de un curso existente.
     *
     * @param id identificador del curso
     * @param curso nuevos datos del curso
     * @return curso actualizado
     */
    @PutMapping("/{id}")
    public Curso actualizar(
            @PathVariable Long id,
            @RequestBody Curso curso) {

        return cursoService.actualizar(id, curso);
    }
}
