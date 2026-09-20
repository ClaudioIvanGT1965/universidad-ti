package cl.universidadti.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Curso;

/**
 * Repositorio de persistencia para cursos académicos.
 */
public interface CursoRepository
        extends JpaRepository<Curso, Long> {

    /**
     * @param codigo código del curso
     * @return {@code true} si ya existe un curso con ese código
     */
    boolean existsByCodigo(String codigo);

    /**
     * @param codigo código del curso
     * @return curso encontrado, si existe
     */
    Optional<Curso> findByCodigo(String codigo);

    /**
     * @return cursos vigentes ordenados por código
     */
    List<Curso> findByVigenteTrueOrderByCodigoAsc();

    /**
     * @return todos los cursos ordenados por código
     */
    List<Curso> findAllByOrderByCodigoAsc();
}
