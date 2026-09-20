package cl.universidadti.controller.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.universidadti.dto.CalificacionDTO;
import cl.universidadti.dto.NotaLoteDTO;
import cl.universidadti.dto.SeccionDTO;
import cl.universidadti.model.Curso;
import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.model.Nota;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.CursoRepository;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.repository.SeccionRepository;
import cl.universidadti.service.CursoService;
import cl.universidadti.service.EstudianteService;
import cl.universidadti.service.InscripcionService;
import cl.universidadti.service.MiCuentaService;
import cl.universidadti.service.NotaService;
import cl.universidadti.service.SeccionService;

/**
 * Controlador MVC para las vistas web de estudiantes y administradores.
 */
@Controller
public class WebController {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final InscripcionService inscripcionService;
    private final MiCuentaService miCuentaService;
    private final EstudianteService estudianteService;
    private final CursoRepository cursoRepository;
    private final CursoService cursoService;
    private final SeccionRepository seccionRepository;
    private final SeccionService seccionService;
    private final NotaService notaService;
    private final NotaRepository notaRepository;

    /**
     * Crea el controlador web con sus servicios y repositorios.
     *
     * @param estudianteRepository repositorio de estudiantes
     * @param estudianteSeccionRepository repositorio de inscripciones
     * @param inscripcionService servicio de inscripciones
     * @param miCuentaService servicio de consultas de cuenta
     * @param estudianteService servicio de estudiantes
     * @param cursoRepository repositorio de cursos
     * @param cursoService servicio de cursos
     * @param seccionRepository repositorio de secciones
     * @param seccionService servicio de secciones
     * @param notaService servicio de notas
     * @param notaRepository repositorio de notas
     */
    public WebController(
            EstudianteRepository estudianteRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            InscripcionService inscripcionService,
            MiCuentaService miCuentaService,
            EstudianteService estudianteService,
            CursoRepository cursoRepository,
            CursoService cursoService,
            SeccionRepository seccionRepository,
            SeccionService seccionService,
            NotaService notaService,
            NotaRepository notaRepository) {

        this.estudianteRepository = estudianteRepository;
        this.estudianteSeccionRepository = estudianteSeccionRepository;
        this.inscripcionService = inscripcionService;
        this.miCuentaService = miCuentaService;
        this.estudianteService = estudianteService;
        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
        this.seccionRepository = seccionRepository;
        this.seccionService = seccionService;
        this.notaService = notaService;
        this.notaRepository = notaRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/estudiante")
    public String estudiante(
            Principal principal,
            Model model) {

        String email
                = principal.getName();

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante "
                                + "asociado al usuario."));

        model.addAttribute(
                "estudiante",
                estudiante);

        return "estudiante/index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/estudiante/cursos")
    public String misCursos(
            Principal principal,
            Model model) {

        String email = principal.getName();

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante "
                                + "asociado al usuario."));

        List<EstudianteSeccion> inscripciones
                = estudianteSeccionRepository
                        .findByEstudianteId(
                                estudiante.getId());

        model.addAttribute(
                "estudiante",
                estudiante);

        model.addAttribute(
                "inscripciones",
                inscripciones);

        return "estudiante/cursos";
    }

    @PostMapping("/estudiante/cursos/retirar/{idInscripcion}")
    public String retirarCurso(
            @PathVariable Long idInscripcion,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            String email = principal.getName();

            inscripcionService.retirarPorEmail(
                    email,
                    idInscripcion);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La inscripción fue retirada correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/estudiante/cursos";
    }

    @GetMapping("/estudiante/notas")
    public String misNotas(
            Principal principal,
            Model model) {

        String email = principal.getName();

        model.addAttribute(
                "cursos",
                miCuentaService.obtenerNotas(email));

        return "estudiante/notas";
    }

    @GetMapping("/estudiante/plan")
    public String planCurricular(Model model) {

        model.addAttribute(
                "cursos",
                miCuentaService.obtenerPlanCurricular());

        return "estudiante/plan";
    }

    @GetMapping("/estudiante/inscripcion")
    public String inscripcionAsignaturas(
            Principal principal,
            Model model) {

        String email = principal.getName();

        model.addAttribute(
                "secciones",
                miCuentaService.obtenerSeccionesDisponibles(email));

        return "estudiante/inscripcion";
    }

    @PostMapping("/estudiante/inscripcion/{idSeccion}")
    public String inscribirAsignatura(
            @PathVariable Long idSeccion,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            String email = principal.getName();

            inscripcionService.inscribirPorEmail(
                    email,
                    idSeccion);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La asignatura fue inscrita correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/estudiante/inscripcion";
    }

    @GetMapping("/estudiante/datos")
    public String misDatos(
            Principal principal,
            Model model) {

        String email = principal.getName();

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante asociado al usuario."));

        model.addAttribute(
                "estudiante",
                estudiante);

        return "estudiante/datos";
    }

    @GetMapping("/admin/estudiantes")
    public String administrarEstudiantes(Model model) {

        model.addAttribute(
                "estudiantes",
                estudianteRepository.findAllByOrderByNombreAsc());

        return "admin/estudiantes";
    }

    @GetMapping("/admin/estudiantes/nuevo")
    public String nuevoEstudiante(Model model) {

        model.addAttribute(
                "estudiante",
                new Estudiante());

        return "admin/estudiante-form";
    }

    @PostMapping("/admin/estudiantes/nuevo")
    public String guardarEstudiante(
            @ModelAttribute Estudiante estudiante,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {

            estudianteService.crear(estudiante);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El estudiante fue registrado correctamente.");

            return "redirect:/admin/estudiantes";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage());

            model.addAttribute(
                    "estudiante",
                    estudiante);

            return "admin/estudiante-form";
        }
    }

    @PostMapping("/admin/estudiantes/{id}/baja")
    public String darBajaEstudiante(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            estudianteService.eliminarLogicamente(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El estudiante fue dado de baja correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/admin/estudiantes";
    }

    @GetMapping("/admin/cursos")
    public String administrarCursos(Model model) {

        model.addAttribute(
                "cursos",
                cursoRepository.findAllByOrderByCodigoAsc());

        return "admin/cursos";
    }

    @GetMapping("/admin/cursos/nuevo")
    public String nuevoCurso(Model model) {

        model.addAttribute(
                "curso",
                new Curso());

        return "admin/curso-form";
    }

    @PostMapping("/admin/cursos/nuevo")
    public String guardarCurso(
            @ModelAttribute Curso curso,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {

            cursoService.crear(curso);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El curso fue registrado correctamente.");

            return "redirect:/admin/cursos";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage());

            model.addAttribute(
                    "curso",
                    curso);

            return "admin/curso-form";
        }
    }

    @PostMapping("/admin/cursos/{id}/baja")
    public String darBajaCurso(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            cursoService.eliminarLogicamente(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El curso fue dado de baja correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/admin/cursos";
    }

    @GetMapping("/admin/secciones")
    public String administrarSecciones(Model model) {

        model.addAttribute(
                "secciones",
                seccionRepository
                        .findAllByOrderByPeriodoDescCursoCodigoAscModuloAsc());

        return "admin/secciones";
    }

    @GetMapping("/admin/secciones/nueva")
    public String nuevaSeccion(Model model) {

        model.addAttribute(
                "seccion",
                new SeccionDTO());

        model.addAttribute(
                "cursos",
                cursoRepository
                        .findByVigenteTrueOrderByCodigoAsc());

        return "admin/seccion-form";
    }

    @PostMapping("/admin/secciones/nueva")
    public String guardarSeccion(
            @ModelAttribute("seccion") SeccionDTO seccion,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {

            seccionService.crear(seccion);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La sección fue creada correctamente.");

            return "redirect:/admin/secciones";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage());

            model.addAttribute(
                    "seccion",
                    seccion);

            model.addAttribute(
                    "cursos",
                    cursoRepository
                            .findByVigenteTrueOrderByCodigoAsc());

            return "admin/seccion-form";
        }
    }

    @PostMapping("/admin/secciones/{id}/eliminar")
    public String eliminarSeccion(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            seccionService.eliminar(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La sección fue eliminada correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/admin/secciones";
    }

    @GetMapping("/admin/notas")
    public String administrarNotas(Model model) {

        model.addAttribute(
                "secciones",
                seccionRepository
                        .findAllByOrderByPeriodoDescCursoCodigoAscModuloAsc());

        return "admin/notas";
    }

    @GetMapping("/admin/notas/seccion/{id}")
    public String gestionarNotasSeccion(
            @PathVariable Long id,
            Model model) {

        // 1. Buscar la sección
        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Sección no encontrada."));

        // 2. Buscar estudiantes inscritos
        List<EstudianteSeccion> inscripciones
                = estudianteSeccionRepository
                        .findBySeccionIdOrderByEstudianteNombreAsc(id);

        // 3. Buscar las notas de cada inscripción
        Map<Long, List<Nota>> notasPorInscripcion
                = new LinkedHashMap<>();

        for (EstudianteSeccion inscripcion : inscripciones) {

            List<Nota> notas
                    = notaRepository
                            .findByEstudianteSeccionIdOrderByNumeroNotaAsc(
                                    inscripcion.getId());

            notasPorInscripcion.put(
                    inscripcion.getId(),
                    notas);
        }

        // 4. Determinar qué evaluaciones ya existen
        Set<Integer> evaluacionesRegistradas
                = new TreeSet<>();

        for (List<Nota> notas : notasPorInscripcion.values()) {

            for (Nota nota : notas) {

                evaluacionesRegistradas.add(
                        nota.getNumeroNota());
            }
        }
        //
        int ponderacionUtilizada = 0;

        Set<Integer> ponderacionesContadas
                = new HashSet<>();

        for (List<Nota> notas : notasPorInscripcion.values()) {

            for (Nota nota : notas) {

                if (ponderacionesContadas.add(
                        nota.getNumeroNota())) {

                    ponderacionUtilizada
                            += nota.getPonderacion();
                }
            }
        }
        int ponderacionRestante = 100 - ponderacionUtilizada;
        // 5. Preparar formulario para una nueva evaluación
        NotaLoteDTO notaLote
                = new NotaLoteDTO();

        List<CalificacionDTO> calificaciones
                = new ArrayList<>();

        for (EstudianteSeccion inscripcion : inscripciones) {

            CalificacionDTO calificacion
                    = new CalificacionDTO();

            calificacion.setIdEstudianteSeccion(
                    inscripcion.getId());

            calificaciones.add(calificacion);
        }

        notaLote.setCalificaciones(calificaciones);

        // 6. Enviar información a Thymeleaf
        model.addAttribute(
                "seccion",
                seccion);

        model.addAttribute(
                "inscripciones",
                inscripciones);

        model.addAttribute(
                "notaLote",
                notaLote);

        model.addAttribute(
                "notasPorInscripcion",
                notasPorInscripcion);

        model.addAttribute(
                "evaluacionesRegistradas",
                evaluacionesRegistradas);
        model.addAttribute(
                "ponderacionUtilizada",
                ponderacionUtilizada);

        model.addAttribute(
                "ponderacionRestante",
                ponderacionRestante);

        return "admin/notas-seccion";
    }

    @PostMapping("/admin/notas/seccion/{id}")
    public String guardarEvaluacion(
            @PathVariable Long id,
            @ModelAttribute("notaLote") NotaLoteDTO notaLote,
            RedirectAttributes redirectAttributes) {

        try {

            notaService.registrarNotasSeccion(
                    id,
                    notaLote);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La evaluación fue registrada correctamente.");

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/admin/notas/seccion/" + id;
    }

}
