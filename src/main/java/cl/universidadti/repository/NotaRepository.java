package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Nota;

/**
 * Repositorio de persistencia para notas académicas.
 */
public interface NotaRepository
        extends JpaRepository<Nota, Long> {

    /**
     * @param idEstudianteSeccion identificador de la inscripción
     * @return notas ordenadas por número ascendente
     */
    List<Nota> findByEstudianteSeccionIdOrderByNumeroNotaAsc(
            Long idEstudianteSeccion);

    /**
     * @return {@code true} si ya existe esa nota para la inscripción
     */
    boolean existsByEstudianteSeccionIdAndNumeroNota(
            Long idEstudianteSeccion,
            Integer numeroNota);

    /**
     * @return {@code true} si la inscripción tiene notas
     */
    boolean existsByEstudianteSeccionId(
            Long idEstudianteSeccion);

    /**
     * @return {@code true} si la sección contiene notas
     */
    boolean existsByEstudianteSeccionSeccionId(
            Long idSeccion);
}
