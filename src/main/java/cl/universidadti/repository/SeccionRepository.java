package cl.universidadti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Seccion;

/**
 * Repositorio de persistencia para secciones académicas.
 */
public interface SeccionRepository
        extends JpaRepository<Seccion, Long> {

    /**
     * @return secciones ordenadas por período, curso y módulo
     */
    List<Seccion> findAllByOrderByPeriodoDescCursoCodigoAscModuloAsc();

}
