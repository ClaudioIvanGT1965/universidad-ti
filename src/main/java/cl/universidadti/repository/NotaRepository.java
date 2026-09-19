package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Nota;

public interface NotaRepository
        extends JpaRepository<Nota, Long> {

    List<Nota> findByEstudianteSeccionIdOrderByNumeroNotaAsc(
            Long idEstudianteSeccion);

    boolean existsByEstudianteSeccionIdAndNumeroNota(
            Long idEstudianteSeccion,
            Integer numeroNota);

    boolean existsByEstudianteSeccionId(
            Long idEstudianteSeccion);

    boolean existsByEstudianteSeccionSeccionId(
            Long idSeccion);
}
