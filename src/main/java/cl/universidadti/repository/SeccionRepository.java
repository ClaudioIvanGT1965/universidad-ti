package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Seccion;

public interface SeccionRepository
        extends JpaRepository<Seccion, Long> {

    List<Seccion> findAllByOrderByPeriodoDescCursoCodigoAscModuloAsc();

}
