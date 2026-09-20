package cl.universidadti.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.universidadti.model.Estudiante;

/**
 * Repositorio de persistencia para estudiantes.
 */
public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {

    /**
     * Comprueba si existe un estudiante con el correo indicado.
     *
     * @param email correo del estudiante
     * @return {@code true} si existe un estudiante con ese correo
     */
    boolean existsByEmail(String email);

    /**
     * Busca un estudiante por correo.
     *
     * @param email correo del estudiante
     * @return estudiante encontrado, si existe
     */
    Optional<Estudiante> findByEmail(String email);

    /**
     * @return estudiantes ordenados alfabéticamente por nombre
     */
    List<Estudiante> findAllByOrderByNombreAsc();

    /**
     * Busca estudiantes vigentes inscritos en un curso.
     *
     * @param codigoCurso código del curso
     * @return estudiantes vigentes ordenados por nombre
     */
    @Query("""
    SELECT DISTINCT e
    FROM Estudiante e
    JOIN EstudianteSeccion es ON es.estudiante.id = e.id
    JOIN es.seccion s
    WHERE s.curso.codigo = :codigoCurso
      AND e.vigente = true
    ORDER BY e.nombre
""")
    List<Estudiante> findEstudiantesVigentesPorCurso(
            @Param("codigoCurso") String codigoCurso);

}
