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

import cl.universidadti.dto.CargaAcademicaDTO;
import cl.universidadti.dto.CursoNotasDTO;
import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.service.EstudianteService;
import cl.universidadti.service.MiCuentaService;

/**
 * Controlador REST para administrar estudiantes y su información académica.
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRestController {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteService estudianteService;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final MiCuentaService miCuentaService;

    public EstudianteRestController(
            EstudianteRepository estudianteRepository,
            EstudianteService estudianteService,
            EstudianteSeccionRepository estudianteSeccionRepository,
            MiCuentaService miCuentaService) {

        this.estudianteRepository
                = estudianteRepository;

        this.estudianteService
                = estudianteService;

        this.estudianteSeccionRepository
                = estudianteSeccionRepository;

        this.miCuentaService
                = miCuentaService;
    }

    /**
     * @return todos los estudiantes registrados
     */
    @GetMapping
    public List<Estudiante> listar() {

        return estudianteRepository.findAll();
    }

    /**
     * Crea un estudiante y su cuenta de acceso.
     *
     * @param estudiante datos del estudiante
     * @return estudiante persistido
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudiante crear(
            @RequestBody Estudiante estudiante) {

        return estudianteService.crear(estudiante);
    }

    /**
     * @param id identificador del estudiante que se desea desactivar
     */
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        estudianteService.eliminarLogicamente(id);
    }

    /**
     * @param codigoCurso código del curso
     * @return estudiantes vigentes del curso
     */
    @GetMapping("/curso/{codigoCurso}")
    public List<Estudiante> estudiantesVigentesPorCurso(
            @PathVariable String codigoCurso) {

        return estudianteRepository
                .findEstudiantesVigentesPorCurso(codigoCurso);
    }

    /**
     * @param id identificador del estudiante
     * @return carga académica del estudiante
     */
    @GetMapping("/{id}/carga")
    public List<CargaAcademicaDTO> obtenerCargaAcademica(
            @PathVariable Long id) {

        if (!estudianteRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Estudiante no encontrado.");
        }

        List<EstudianteSeccion> inscripciones
                = estudianteSeccionRepository
                        .findByEstudianteId(id);

        return inscripciones.stream()
                .map(inscripcion
                        -> new CargaAcademicaDTO(
                        inscripcion.getId(),
                        inscripcion.getSeccion()
                                .getCurso()
                                .getCodigo(),
                        inscripcion.getSeccion()
                                .getCurso()
                                .getNombre(),
                        inscripcion.getSeccion()
                                .getPeriodo(),
                        inscripcion.getSeccion()
                                .getModulo(),
                        inscripcion.getPromedio()
                )
                )
                .toList();
    }

    /**
     * @param id identificador del estudiante
     * @return notas agrupadas por curso
     */
    @GetMapping("/{id}/notas")
    public List<CursoNotasDTO> obtenerNotasEstudiante(
            @PathVariable Long id) {

        return miCuentaService
                .obtenerNotasPorIdEstudiante(id);
    }

    /**
     * @param id identificador del estudiante
     * @param estudiante nuevos datos
     * @return estudiante actualizado
     */
    @PutMapping("/{id}")
    public Estudiante actualizar(
            @PathVariable Long id,
            @RequestBody Estudiante estudiante) {

        return estudianteService
                .actualizar(id, estudiante);
    }

}
