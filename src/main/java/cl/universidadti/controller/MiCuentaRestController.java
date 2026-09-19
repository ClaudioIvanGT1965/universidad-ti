package cl.universidadti.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.universidadti.dto.CursoNotasDTO;
import cl.universidadti.dto.CursoPlanDTO;
import cl.universidadti.dto.SeccionDisponibleDTO;
import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.service.InscripcionService;
import cl.universidadti.service.MiCuentaService;

@RestController
@RequestMapping("/api/mi-cuenta")
public class MiCuentaRestController {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final MiCuentaService miCuentaService;
    private final InscripcionService inscripcionService;

    public MiCuentaRestController(
            EstudianteRepository estudianteRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            MiCuentaService miCuentaService,
            InscripcionService inscripcionService) {

        this.estudianteRepository = estudianteRepository;

        this.estudianteSeccionRepository
                = estudianteSeccionRepository;

        this.miCuentaService
                = miCuentaService;

        this.inscripcionService
                = inscripcionService;
    }

    @GetMapping
    public Estudiante misDatos(Authentication authentication) {

        String email = authentication.getName();

        return estudianteRepository
                .findByEmail(email)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "No existe un estudiante asociado "
                        + "al usuario autenticado."));
    }

    @GetMapping("/carga")
    public List<EstudianteSeccion> miCarga(
            Authentication authentication) {

        String email = authentication.getName();

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante asociado "
                                + "al usuario autenticado."));

        return estudianteSeccionRepository
                .findByEstudianteId(
                        estudiante.getId());
    }

    @GetMapping("/notas")
    public List<CursoNotasDTO> misNotas(
            Authentication authentication) {

        String email = authentication.getName();

        return miCuentaService.obtenerNotas(email);
    }

    @GetMapping("/plan")
    public List<CursoPlanDTO> miPlanCurricular() {

        return miCuentaService.obtenerPlanCurricular();
    }

    @GetMapping("/secciones-disponibles")
    public List<SeccionDisponibleDTO>
            seccionesDisponibles(
                    Authentication authentication) {

        String email
                = authentication.getName();

        return miCuentaService
                .obtenerSeccionesDisponibles(email);
    }

    @PostMapping("/inscripciones/{idSeccion}")
    public EstudianteSeccion inscribirme(
            @PathVariable Long idSeccion,
            Authentication authentication) {

        String email
                = authentication.getName();

        return inscripcionService
                .inscribirPorEmail(
                        email,
                        idSeccion);
    }

    @DeleteMapping("/inscripciones/{idInscripcion}")
    public void retirarme(
            @PathVariable Long idInscripcion,
            Authentication authentication) {

        String email
                = authentication.getName();

        inscripcionService
                .retirarPorEmail(
                        email,
                        idInscripcion);
    }
}
