package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.EstudianteSeccion;

public interface EstudianteSeccionRepository
        extends JpaRepository<EstudianteSeccion, Long> {

    List<EstudianteSeccion> findByEstudianteId(
            Long idEstudiante);

    List<EstudianteSeccion> findBySeccionId(
            Long idSeccion);

    boolean existsByEstudianteIdAndSeccionCursoCodigoAndSeccionPeriodo(
            Long idEstudiante,
            String codigoCurso,
            Integer periodo);

    long countBySeccionId(Long idSeccion);

    List<EstudianteSeccion>
            findBySeccionIdOrderByEstudianteNombreAsc(Long idSeccion);
}
