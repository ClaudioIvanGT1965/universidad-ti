package cl.universidadti.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.SeccionDTO;
import cl.universidadti.model.Curso;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.CursoRepository;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.repository.SeccionRepository;

/**
 * Servicio que valida y administra las secciones de cursos.
 */
@Service
public class SeccionService {

    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final NotaRepository notaRepository;

    /**
     * Crea el servicio de secciones.
     *
     * @param seccionRepository repositorio de secciones
     * @param cursoRepository repositorio de cursos
     * @param notaRepository repositorio de notas
     */
    public SeccionService(
            SeccionRepository seccionRepository,
            CursoRepository cursoRepository,
            NotaRepository notaRepository) {

        this.seccionRepository = seccionRepository;
        this.cursoRepository = cursoRepository;
        this.notaRepository = notaRepository;
    }

    /**
     * Valida y crea una sección para un curso vigente.
     *
     * @param dto datos de la sección
     * @return sección persistida
     */
    @Transactional
    public Seccion crear(SeccionDTO dto) {

        if (dto.getCodigoCurso() == null
                || dto.getCodigoCurso().isBlank()) {
            throw new IllegalArgumentException(
                    "El código del curso es obligatorio.");
        }

        Curso curso = cursoRepository.findByCodigo(dto.getCodigoCurso())
                .orElseThrow(() -> new IllegalArgumentException(
                "El curso no existe."));

        if (!Boolean.TRUE.equals(curso.getVigente())) {
            throw new IllegalArgumentException(
                    "No se puede crear una sección para un curso no vigente.");
        }

        if (dto.getPeriodo() == null || dto.getPeriodo() <= 0) {
            throw new IllegalArgumentException(
                    "El período es obligatorio y debe ser válido.");
        }
        if (dto.getTopeEstudiantes() == null
                || dto.getTopeEstudiantes() <= 0) {
            throw new IllegalArgumentException(
                    "El tope de estudiantes debe ser mayor que cero.");
        }
        if (dto.getTotalNotas() == null || dto.getTotalNotas() <= 0) {
            throw new IllegalArgumentException(
                    "El total de notas debe ser mayor que cero.");
        }
        if (dto.getFechaIni() == null || dto.getFechaTer() == null) {
            throw new IllegalArgumentException(
                    "Las fechas de inicio y término son obligatorias.");
        }
        if (dto.getFechaTer().isBefore(dto.getFechaIni())) {
            throw new IllegalArgumentException(
                    "La fecha de término no puede ser anterior a la fecha de inicio.");
        }

        Seccion seccion = new Seccion();
        seccion.setPeriodo(dto.getPeriodo());
        seccion.setCurso(curso);
        seccion.setTopeEstudiantes(dto.getTopeEstudiantes());
        seccion.setTotalNotas(dto.getTotalNotas());
        seccion.setFechaIni(dto.getFechaIni());
        seccion.setFechaTer(dto.getFechaTer());
        seccion.setModulo(dto.getModulo());
        return seccionRepository.save(seccion);
    }

    /**
     * Elimina una sección que no tenga notas asociadas.
     *
     * @param idSeccion identificador de la sección
     * @throws IllegalArgumentException si no existe o tiene notas
     */
    @Transactional
    public void eliminar(Long idSeccion) {

        Seccion seccion = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new IllegalArgumentException(
                "La sección no existe."));

        if (notaRepository.existsByEstudianteSeccionSeccionId(idSeccion)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la sección porque existen notas registradas.");
        }
        seccionRepository.delete(seccion);
    }

    /**
     * Actualiza una sección que aún no tiene notas registradas.
     *
     * @param id identificador de la sección
     * @param dto nuevos datos de la sección
     * @return sección actualizada
     */
    @Transactional
    public Seccion actualizar(Long id, SeccionDTO dto) {

        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "Sección no encontrada."));

        if (notaRepository.existsByEstudianteSeccionSeccionId(id)) {
            throw new IllegalArgumentException(
                    "Una sección con notas registradas no puede ser modificada.");
        }

        Curso curso = cursoRepository.findByCodigo(dto.getCodigoCurso())
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado."));

        if (!Boolean.TRUE.equals(curso.getVigente())) {
            throw new IllegalArgumentException(
                    "El curso no se encuentra vigente.");
        }
        if (dto.getPeriodo() == null || dto.getPeriodo() <= 0) {
            throw new IllegalArgumentException("El período debe ser válido.");
        }
        if (dto.getTopeEstudiantes() == null
                || dto.getTopeEstudiantes() <= 0) {
            throw new IllegalArgumentException(
                    "El tope de estudiantes debe ser mayor que cero.");
        }
        if (dto.getTotalNotas() == null || dto.getTotalNotas() <= 0) {
            throw new IllegalArgumentException(
                    "El total de notas debe ser mayor que cero.");
        }
        if (dto.getFechaIni() == null || dto.getFechaTer() == null) {
            throw new IllegalArgumentException(
                    "Las fechas de inicio y término son obligatorias.");
        }
        if (dto.getFechaTer().isBefore(dto.getFechaIni())) {
            throw new IllegalArgumentException(
                    "La fecha de término no puede ser anterior a la fecha de inicio.");
        }

        seccion.setPeriodo(dto.getPeriodo());
        seccion.setCurso(curso);
        seccion.setTopeEstudiantes(dto.getTopeEstudiantes());
        seccion.setTotalNotas(dto.getTotalNotas());
        seccion.setFechaIni(dto.getFechaIni());
        seccion.setFechaTer(dto.getFechaTer());
        seccion.setModulo(dto.getModulo());
        return seccionRepository.save(seccion);
    }
}
