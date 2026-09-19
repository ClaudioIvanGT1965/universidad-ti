package cl.universidadti.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Curso;

public interface CursoRepository
        extends JpaRepository<Curso, Long> {

    boolean existsByCodigo(String codigo);

    Optional<Curso> findByCodigo(String codigo);

    List<Curso> findByVigenteTrueOrderByCodigoAsc();

    List<Curso> findAllByOrderByCodigoAsc();
}
