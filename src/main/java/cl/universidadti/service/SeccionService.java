package cl.universidadti.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.SeccionDTO;
import cl.universidadti.model.Curso;
import cl.universidadti.model.Seccion;
import cl.universidadti.repository.CursoRepository;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.repository.SeccionRepository;

@Service
public class SeccionService {

    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final NotaRepository notaRepository;

    public SeccionService(
            SeccionRepository seccionRepository,
            CursoRepository cursoRepository,
            NotaRepository notaRepository) {

        this.seccionRepository = seccionRepository;
        this.cursoRepository = cursoRepository;
        this.notaRepository = notaRepository;
    }

    @Transactional
    public Seccion crear(SeccionDTO dto) {

        // 1. Validar código de curso
        if (dto.getCodigoCurso() == null
                || dto.getCodigoCurso().isBlank()) {

            throw new IllegalArgumentException(
                    "El código del curso es obligatorio.");
        }

        // 2. Buscar curso
        Curso curso
                = cursoRepository
                        .findByCodigo(dto.getCodigoCurso())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "El curso no existe."));

        // 3. Comprobar vigencia
        if (!Boolean.TRUE.equals(curso.getVigente())) {

            throw new IllegalArgumentException(
                    "No se puede crear una sección "
                    + "para un curso no vigente.");
        }

        // 4. Validar período
        if (dto.getPeriodo() == null
                || dto.getPeriodo() <= 0) {

            throw new IllegalArgumentException(
                    "El período es obligatorio y debe ser válido.");
        }

        // 5. Validar capacidad
        if (dto.getTopeEstudiantes() == null
                || dto.getTopeEstudiantes() <= 0) {

            throw new IllegalArgumentException(
                    "El tope de estudiantes debe ser mayor que cero.");
        }

        // 6. Validar cantidad de notas
        if (dto.getTotalNotas() == null
                || dto.getTotalNotas() <= 0) {

            throw new IllegalArgumentException(
                    "El total de notas debe ser mayor que cero.");
        }

        // 7. Validar fechas
        if (dto.getFechaIni() == null
                || dto.getFechaTer() == null) {

            throw new IllegalArgumentException(
                    "Las fechas de inicio y término son obligatorias.");
        }

        if (dto.getFechaTer().isBefore(dto.getFechaIni())) {

            throw new IllegalArgumentException(
                    "La fecha de término no puede ser "
                    + "anterior a la fecha de inicio.");
        }

        // 8. Crear objeto Seccion
        Seccion seccion = new Seccion();

        seccion.setPeriodo(dto.getPeriodo());
        seccion.setCurso(curso);
        seccion.setTopeEstudiantes(dto.getTopeEstudiantes());
        seccion.setTotalNotas(dto.getTotalNotas());
        seccion.setFechaIni(dto.getFechaIni());
        seccion.setFechaTer(dto.getFechaTer());
        seccion.setModulo(dto.getModulo());

        // 9. Guardar
        return seccionRepository.save(seccion);
    }

    @Transactional
    public void eliminar(Long idSeccion) {

        // 1. Comprobar existencia
        Seccion seccion
                = seccionRepository
                        .findById(idSeccion)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "La sección no existe."));

        // 2. Comprobar notas
        boolean tieneNotas
                = notaRepository
                        .existsByEstudianteSeccionSeccionId(
                                idSeccion);

        if (tieneNotas) {

            throw new IllegalArgumentException(
                    "No se puede eliminar la sección "
                    + "porque existen notas registradas.");
        }

        // 3. Eliminar
        seccionRepository.delete(seccion);
    }

    @Transactional
    public Seccion actualizar(Long id, SeccionDTO dto) {

        Seccion seccion = seccionRepository
                .findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Sección no encontrada."));

        // Una sección con notas queda bloqueada
        if (notaRepository
                .existsByEstudianteSeccionSeccionId(id)) {

            throw new IllegalArgumentException(
                    "Una sección con notas registradas no puede ser modificada.");
        }

        Curso curso = cursoRepository
                .findByCodigo(dto.getCodigoCurso())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Curso no encontrado."));

        if (!Boolean.TRUE.equals(curso.getVigente())) {
            throw new IllegalArgumentException(
                    "El curso no se encuentra vigente.");
        }

        if (dto.getPeriodo() == null
                || dto.getPeriodo() <= 0) {

            throw new IllegalArgumentException(
                    "El período debe ser válido.");
        }

        if (dto.getTopeEstudiantes() == null
                || dto.getTopeEstudiantes() <= 0) {

            throw new IllegalArgumentException(
                    "El tope de estudiantes debe ser mayor que cero.");
        }

        if (dto.getTotalNotas() == null
                || dto.getTotalNotas() <= 0) {

            throw new IllegalArgumentException(
                    "El total de notas debe ser mayor que cero.");
        }

        if (dto.getFechaIni() == null
                || dto.getFechaTer() == null) {

            throw new IllegalArgumentException(
                    "Las fechas de inicio y término son obligatorias.");
        }

        if (dto.getFechaTer()
                .isBefore(dto.getFechaIni())) {

            throw new IllegalArgumentException(
                    "La fecha de término no puede ser anterior a la fecha de inicio.");
        }

        seccion.setPeriodo(dto.getPeriodo());
        seccion.setCurso(curso);
        seccion.setTopeEstudiantes(
                dto.getTopeEstudiantes());
        seccion.setTotalNotas(
                dto.getTotalNotas());
        seccion.setFechaIni(dto.getFechaIni());
        seccion.setFechaTer(dto.getFechaTer());
        seccion.setModulo(dto.getModulo());

        return seccionRepository.save(seccion);
    }
}
