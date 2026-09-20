package cl.universidadti.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.InscripcionDTO;
import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.repository.SeccionRepository;

/**
 * Servicio que controla las inscripciones y retiros de estudiantes.
 */
@Service
public class InscripcionService {

    private final EstudianteRepository estudianteRepository;
    private final SeccionRepository seccionRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final NotaRepository notaRepository;

    /**
     * Crea el servicio de inscripciones.
     *
     * @param estudianteRepository repositorio de estudiantes
     * @param seccionRepository repositorio de secciones
     * @param estudianteSeccionRepository repositorio de inscripciones
     * @param notaRepository repositorio de notas
     */
    public InscripcionService(
            EstudianteRepository estudianteRepository,
            SeccionRepository seccionRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            NotaRepository notaRepository) {

        this.estudianteRepository = estudianteRepository;
        this.seccionRepository = seccionRepository;
        this.estudianteSeccionRepository = estudianteSeccionRepository;
        this.notaRepository = notaRepository;
    }

    /**
     * Inscribe a un estudiante verificando vigencia, duplicidad y cupos.
     *
     * @param dto identificadores del estudiante y de la sección
     * @return inscripción creada
     */
    @Transactional
    public EstudianteSeccion inscribir(InscripcionDTO dto) {

        Estudiante estudiante = estudianteRepository
                .findById(dto.getIdEstudiante())
                .orElseThrow(() -> new IllegalArgumentException(
                "El estudiante no existe."));

        if (!Boolean.TRUE.equals(estudiante.getVigente())) {
            throw new IllegalArgumentException(
                    "El estudiante no está vigente.");
        }

        Seccion seccion = seccionRepository.findById(dto.getIdSeccion())
                .orElseThrow(() -> new IllegalArgumentException(
                "La sección no existe."));

        if (!Boolean.TRUE.equals(seccion.getCurso().getVigente())) {
            throw new IllegalArgumentException("El curso no está vigente.");
        }

        boolean yaInscrito = estudianteSeccionRepository
                .existsByEstudianteIdAndSeccionCursoCodigoAndSeccionPeriodo(
                        estudiante.getId(),
                        seccion.getCurso().getCodigo(),
                        seccion.getPeriodo());

        if (yaInscrito) {
            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en el curso "
                    + seccion.getCurso().getNombre()
                    + " durante el período " + seccion.getPeriodo() + ".");
        }

        long inscritos = estudianteSeccionRepository
                .countBySeccionId(seccion.getId());

        if (inscritos >= seccion.getTopeEstudiantes()) {
            throw new IllegalArgumentException(
                    "La sección no tiene cupos disponibles.");
        }

        EstudianteSeccion inscripcion = new EstudianteSeccion();
        inscripcion.setEstudiante(estudiante);
        inscripcion.setSeccion(seccion);
        inscripcion.setPromedio(BigDecimal.ZERO);
        return estudianteSeccionRepository.save(inscripcion);
    }

    /**
     * Retira una inscripción que no tenga notas registradas.
     *
     * @param idInscripcion identificador de la inscripción
     */
    @Transactional
    public void retirar(Long idInscripcion) {

        EstudianteSeccion inscripcion = estudianteSeccionRepository
                .findById(idInscripcion)
                .orElseThrow(() -> new IllegalArgumentException(
                "La inscripción no existe."));

        if (notaRepository.existsByEstudianteSeccionId(idInscripcion)) {
            throw new IllegalArgumentException(
                    "No se puede retirar la inscripción "
                    + "porque el estudiante ya tiene notas registradas.");
        }
        estudianteSeccionRepository.delete(inscripcion);
    }

    /**
     * Inscribe al estudiante identificado por su correo.
     *
     * @param email correo del estudiante autenticado
     * @param idSeccion identificador de la sección
     * @return inscripción creada
     */
    @Transactional
    public EstudianteSeccion inscribirPorEmail(
            String email,
            Long idSeccion) {

        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe un estudiante asociado "
                + "al usuario autenticado."));
        InscripcionDTO dto = new InscripcionDTO();
        dto.setIdEstudiante(estudiante.getId());
        dto.setIdSeccion(idSeccion);
        return inscribir(dto);
    }

    /**
     * Retira una inscripción verificando que pertenezca al correo indicado.
     *
     * @param email correo del estudiante autenticado
     * @param idInscripcion identificador de la inscripción
     */
    @Transactional
    public void retirarPorEmail(
            String email,
            Long idInscripcion) {

        Estudiante estudiante = estudianteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe un estudiante asociado "
                + "al usuario autenticado."));

        EstudianteSeccion inscripcion = estudianteSeccionRepository
                .findById(idInscripcion)
                .orElseThrow(() -> new IllegalArgumentException(
                "La inscripción no existe."));

        if (!inscripcion.getEstudiante().getId().equals(estudiante.getId())) {
            throw new IllegalArgumentException(
                    "La inscripción no pertenece "
                    + "al estudiante autenticado.");
        }
        retirar(idInscripcion);
    }

}
