package cl.universidadti.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.CursoNotasDTO;
import cl.universidadti.dto.CursoPlanDTO;
import cl.universidadti.dto.NotaDetalleDTO;
import cl.universidadti.dto.SeccionDisponibleDTO;
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

/**
 * Servicio de consultas académicas para las cuentas de estudiantes.
 */
@Service
public class MiCuentaService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final NotaRepository notaRepository;
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;

    /**
     * Crea el servicio de consultas de cuenta.
     *
     * @param estudianteRepository repositorio de estudiantes
     * @param estudianteSeccionRepository repositorio de inscripciones
     * @param notaRepository repositorio de notas
     * @param cursoRepository repositorio de cursos
     * @param seccionRepository repositorio de secciones
     */
    public MiCuentaService(
            EstudianteRepository estudianteRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            NotaRepository notaRepository,
            CursoRepository cursoRepository,
            SeccionRepository seccionRepository) {

        this.estudianteRepository = estudianteRepository;
        this.estudianteSeccionRepository = estudianteSeccionRepository;
        this.notaRepository = notaRepository;
        this.cursoRepository = cursoRepository;
        this.seccionRepository = seccionRepository;
    }

    /**
     * Obtiene las notas del estudiante asociado a un correo.
     *
     * @param email correo del estudiante
     * @return notas agrupadas por curso
     */
    @Transactional(readOnly = true)
    public List<CursoNotasDTO> obtenerNotas(String email) {

        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                "Estudiante no encontrado."));
        return obtenerNotasPorEstudiante(estudiante);
    }

    /**
     * Obtiene las notas de un estudiante por su identificador.
     *
     * @param idEstudiante identificador del estudiante
     * @return notas agrupadas por curso
     */
    public List<CursoNotasDTO> obtenerNotasPorIdEstudiante(
            Long idEstudiante) {

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new IllegalArgumentException(
                "Estudiante no encontrado."));
        return obtenerNotasPorEstudiante(estudiante);
    }

    private List<CursoNotasDTO> obtenerNotasPorEstudiante(
            Estudiante estudiante) {

        List<EstudianteSeccion> inscripciones = estudianteSeccionRepository
                .findByEstudianteId(estudiante.getId());
        List<CursoNotasDTO> resultado = new ArrayList<>();

        for (EstudianteSeccion inscripcion : inscripciones) {
            List<Nota> notas = notaRepository
                    .findByEstudianteSeccionIdOrderByNumeroNotaAsc(
                            inscripcion.getId());
            List<NotaDetalleDTO> detalleNotas = new ArrayList<>();

            for (Nota nota : notas) {
                NotaDetalleDTO detalle = new NotaDetalleDTO();
                detalle.setNumeroNota(nota.getNumeroNota());
                detalle.setTitulo(nota.getTitulo());
                detalle.setFecha(nota.getFecha());
                detalle.setNota(nota.getNota());
                detalle.setPonderacion(nota.getPonderacion());
                detalleNotas.add(detalle);
            }

            CursoNotasDTO curso = new CursoNotasDTO();
            curso.setCodigoCurso(inscripcion.getSeccion().getCurso().getCodigo());
            curso.setNombreCurso(inscripcion.getSeccion().getCurso().getNombre());
            curso.setPeriodo(inscripcion.getSeccion().getPeriodo());
            curso.setModulo(inscripcion.getSeccion().getModulo());
            curso.setPromedio(inscripcion.getPromedio());
            curso.setNotas(detalleNotas);
            resultado.add(curso);
        }
        return resultado;
    }

    /**
     * @return cursos vigentes del plan curricular
     */
    @Transactional(readOnly = true)
    public List<CursoPlanDTO> obtenerPlanCurricular() {

        List<Curso> cursos = cursoRepository.findByVigenteTrueOrderByCodigoAsc();
        List<CursoPlanDTO> resultado = new ArrayList<>();

        for (Curso curso : cursos) {
            resultado.add(new CursoPlanDTO(
                    curso.getId(),
                    curso.getCodigo(),
                    curso.getNombre(),
                    curso.getDescripcion()));
        }
        return resultado;
    }

    /**
     * Obtiene las secciones vigentes con cupos para un estudiante.
     *
     * @param email correo del estudiante
     * @return secciones disponibles
     */
    @Transactional(readOnly = true)
    public List<SeccionDisponibleDTO> obtenerSeccionesDisponibles(
            String email) {

        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe un estudiante asociado "
                + "al usuario autenticado."));
        List<Seccion> secciones = seccionRepository.findAll();
        List<SeccionDisponibleDTO> resultado = new ArrayList<>();

        for (Seccion seccion : secciones) {
            if (!Boolean.TRUE.equals(seccion.getCurso().getVigente())) {
                continue;
            }

            boolean yaInscrito = estudianteSeccionRepository
                    .existsByEstudianteIdAndSeccionCursoCodigoAndSeccionPeriodo(
                            estudiante.getId(),
                            seccion.getCurso().getCodigo(),
                            seccion.getPeriodo());
            if (yaInscrito) {
                continue;
            }

            long inscritos = estudianteSeccionRepository
                    .countBySeccionId(seccion.getId());
            int cuposDisponibles = seccion.getTopeEstudiantes()
                    - (int) inscritos;
            if (cuposDisponibles <= 0) {
                continue;
            }

            resultado.add(new SeccionDisponibleDTO(
                    seccion.getId(),
                    seccion.getCurso().getCodigo(),
                    seccion.getCurso().getNombre(),
                    seccion.getPeriodo(),
                    seccion.getModulo(),
                    cuposDisponibles,
                    seccion.getFechaIni(),
                    seccion.getFechaTer()));
        }
        return resultado;
    }

}
