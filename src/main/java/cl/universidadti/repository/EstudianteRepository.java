package cl.universidadti.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.universidadti.model.Estudiante;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {

    boolean existsByEmail(String email);

    Optional<Estudiante> findByEmail(String email);

    List<Estudiante> findAllByOrderByNombreAsc();

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
