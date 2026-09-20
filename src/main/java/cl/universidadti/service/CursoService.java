package cl.universidadti.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.model.Curso;
import cl.universidadti.repository.CursoRepository;

/**
 * Servicio que contiene las reglas de negocio para la gestión de cursos.
 */ @Service
public class CursoService {

    private final CursoRepository cursoRepository;

    /**
     * Crea el servicio con el repositorio de cursos requerido.
     *
     * @param cursoRepository repositorio de cursos
     */
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * Valida y persiste un curso nuevo como vigente.
     *
     * @param curso curso que se desea registrar
     * @return curso persistido
     * @throws IllegalArgumentException si falta ya está registrado
     */
    @Transactional
    public Curso crear(Curso curso) {

        // 1. Validar código
        if (curso.getCodigo() == null
                || curso.getCodigo().isBlank()) {

            throw new IllegalArgumentException(
                    "El código del curso es obligatorio.");
        }

        // 2. Validar nombre
        if (curso.getNombre() == null
                || curso.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del curso es obligatorio.");
        }

        // 3. Validar descripción
        if (curso.getDescripcion() == null
                || curso.getDescripcion().isBlank()) {

            throw new IllegalArgumentException(
                    "La descripción del curso es obligatoria.");
        }

        // 4. Verificar código duplicado
        if (cursoRepository.existsByCodigo(
                curso.getCodigo())) {

            throw new IllegalArgumentException(
                    "Ya existe un curso con el código "
                    + curso.getCodigo() + ".");
        }

        // 5. Los controla el servidor
        curso.setId(null);
        curso.setVigente(true);

        // 6. Guardar
        return cursoRepository.save(curso);
    }

    /**
     * Desactiva un curso sin eliminar su registro de la base de datos.
     *
     * @param idCurso identificador del curso que se desea desactivar
     * @throws IllegalArgumentException si el curso no existe o ya está inactivo
     */
    @Transactional
    public void eliminarLogicamente(Long idCurso) {

        Curso curso
                = cursoRepository
                        .findById(idCurso)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "El curso no existe."));

        if (!Boolean.TRUE.equals(curso.getVigente())) {

            throw new IllegalArgumentException(
                    "El curso ya se encuentra eliminado.");
        }

        curso.setVigente(false);

        cursoRepository.save(curso);
    }

    /**
     * Actualiza el nombre y la descripción de un curso.
     *
     * @param id identificador del curso que se desea actualizar
     * @param datos valores editables recibidos
     * @return curso actualizado
     * @throws IllegalArgumentException si el c vacío
     */
    @Transactional
    public Curso actualizar(Long id, Curso datos) {

        Curso curso = cursoRepository
                .findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Curso no encontrado."));

        if (datos.getNombre() == null
                || datos.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del curso es obligatorio.");
        }

        curso.setNombre(datos.getNombre());
        curso.setDescripcion(datos.getDescripcion());

        return cursoRepository.save(curso);
    }
}
