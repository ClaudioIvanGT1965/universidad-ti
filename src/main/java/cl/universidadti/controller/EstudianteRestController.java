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

    @GetMapping
    public List<Estudiante> listar() {

        return estudianteRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudiante crear(
            @RequestBody Estudiante estudiante) {

        return estudianteService.crear(estudiante);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        estudianteService.eliminarLogicamente(id);
    }

    @GetMapping("/curso/{codigoCurso}")
    public List<Estudiante> estudiantesVigentesPorCurso(
            @PathVariable String codigoCurso) {

        return estudianteRepository
                .findEstudiantesVigentesPorCurso(codigoCurso);
    }

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

    @GetMapping("/{id}/notas")
    public List<CursoNotasDTO> obtenerNotasEstudiante(
            @PathVariable Long id) {

        return miCuentaService
                .obtenerNotasPorIdEstudiante(id);
    }

    @PutMapping("/{id}")
    public Estudiante actualizar(
            @PathVariable Long id,
            @RequestBody Estudiante estudiante) {

        return estudianteService
                .actualizar(id, estudiante);
    }

}
