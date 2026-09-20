package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.EstudianteSeccion;

/**
 * Repositorio de inscripciones entre estudiantes y secciones.
 */
public interface EstudianteSeccionRepository
        extends JpaRepository<EstudianteSeccion, Long> {

    /**
     * @param idEstudiante identificador del estudiante
     * @return inscripciones del estudiante
     */
    List<EstudianteSeccion> findByEstudianteId(
            Long idEstudiante);

    /**
     * @param idSeccion identificador de la sección
     * @return inscripciones de la sección
     */
    List<EstudianteSeccion> findBySeccionId(
            Long idSeccion);

    /**
     * Comprueba si un estudiante ya cursa el mismo curso en un período.
     *
     * @param idEstudiante identificador del estudiante
     * @param codigoCurso código del curso
     * @param periodo período académico
     * @return {@code true} si ya existe la inscripción
     */
    boolean existsByEstudianteIdAndSeccionCursoCodigoAndSeccionPeriodo(
            Long idEstudiante,
            String codigoCurso,
            Integer periodo);

    /**
     * @param idSeccion identificador de la sección
     * @return cantidad de estudiantes inscritos
     */
    long countBySeccionId(Long idSeccion);

    /**
     * @param idSeccion identificador de la sección
     * @return inscripciones ordenadas por nombre del estudiante
     */
    List<EstudianteSeccion>
            findBySeccionIdOrderByEstudianteNombreAsc(Long idSeccion);
}
