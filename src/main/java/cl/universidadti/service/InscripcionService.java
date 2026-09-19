package cl.universidadti.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.InscripcionDTO;
import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.repository.SeccionRepository;

@Service
public class InscripcionService {

    private final EstudianteRepository estudianteRepository;
    private final SeccionRepository seccionRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final NotaRepository notaRepository;

    public InscripcionService(
            EstudianteRepository estudianteRepository,
            SeccionRepository seccionRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            NotaRepository notaRepository) {

        this.estudianteRepository = estudianteRepository;
        this.seccionRepository = seccionRepository;
        this.estudianteSeccionRepository = estudianteSeccionRepository;
        this.notaRepository = notaRepository;
    }

    @Transactional
    public EstudianteSeccion inscribir(InscripcionDTO dto) {

        // 1. Buscar estudiante
        Estudiante estudiante
                = estudianteRepository
                        .findById(dto.getIdEstudiante())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "El estudiante no existe."));

        // 2. Comprobar que esté vigente
        if (!Boolean.TRUE.equals(estudiante.getVigente())) {

            throw new IllegalArgumentException(
                    "El estudiante no está vigente.");
        }

        // 3. Buscar sección
        Seccion seccion
                = seccionRepository
                        .findById(dto.getIdSeccion())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "La sección no existe."));

        // 4. Comprobar que el curso esté vigente
        if (!Boolean.TRUE.equals(
                seccion.getCurso().getVigente())) {

            throw new IllegalArgumentException(
                    "El curso no está vigente.");
        }

        // 5. Evitar repetir curso en el mismo período
        boolean yaInscrito
                = estudianteSeccionRepository
                        .existsByEstudianteIdAndSeccionCursoCodigoAndSeccionPeriodo(
                                estudiante.getId(),
                                seccion.getCurso().getCodigo(),
                                seccion.getPeriodo());

        if (yaInscrito) {

            throw new IllegalArgumentException(
                    "El estudiante ya está inscrito en el curso "
                    + seccion.getCurso().getNombre()
                    + " durante el período "
                    + seccion.getPeriodo()
                    + ".");
        }

        // 6. Revisar capacidad
        long inscritos
                = estudianteSeccionRepository
                        .countBySeccionId(seccion.getId());

        if (inscritos >= seccion.getTopeEstudiantes()) {

            throw new IllegalArgumentException(
                    "La sección no tiene cupos disponibles.");
        }

        // 7. Crear inscripción
        EstudianteSeccion inscripcion
                = new EstudianteSeccion();

        inscripcion.setEstudiante(estudiante);
        inscripcion.setSeccion(seccion);

        // Seguimos tu esquema actual:
        // 0 significa promedio aún no calculado.
        inscripcion.setPromedio(BigDecimal.ZERO);

        // 8. Guardar
        return estudianteSeccionRepository.save(inscripcion);
    }

    @Transactional
    public void retirar(Long idInscripcion) {

        // 1. Comprobar que la inscripción existe
        EstudianteSeccion inscripcion
                = estudianteSeccionRepository
                        .findById(idInscripcion)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "La inscripción no existe."));

        // 2. Comprobar si existen notas
        boolean tieneNotas
                = notaRepository
                        .existsByEstudianteSeccionId(
                                idInscripcion);

        if (tieneNotas) {

            throw new IllegalArgumentException(
                    "No se puede retirar la inscripción "
                    + "porque el estudiante ya tiene notas registradas.");
        }

        // 3. Eliminar inscripción
        estudianteSeccionRepository.delete(inscripcion);
    }

    @Transactional
    public EstudianteSeccion inscribirPorEmail(
            String email,
            Long idSeccion) {

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante asociado "
                                + "al usuario autenticado."));
        InscripcionDTO dto = new InscripcionDTO();

        dto.setIdEstudiante(estudiante.getId());
        dto.setIdSeccion(idSeccion);

        return inscribir(dto);
    }

    @Transactional
    public void retirarPorEmail(
            String email,
            Long idInscripcion) {

        Estudiante estudiante
                = estudianteRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe un estudiante asociado "
                                + "al usuario autenticado."));

        EstudianteSeccion inscripcion
                = estudianteSeccionRepository
                        .findById(idInscripcion)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "La inscripción no existe."));

        if (!inscripcion
                .getEstudiante()
                .getId()
                .equals(estudiante.getId())) {

            throw new IllegalArgumentException(
                    "La inscripción no pertenece "
                    + "al estudiante autenticado.");
        }

        retirar(idInscripcion);
    }

}
