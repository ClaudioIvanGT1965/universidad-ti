package cl.universidadti.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.dto.CalificacionDTO;
import cl.universidadti.dto.NotaLoteDTO;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.model.Nota;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.repository.NotaRepository;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;

    public NotaService(
            NotaRepository notaRepository,
            EstudianteSeccionRepository estudianteSeccionRepository) {

        this.notaRepository = notaRepository;
        this.estudianteSeccionRepository = estudianteSeccionRepository;
    }

    @Transactional
    public Nota registrarNota(Nota nota) {

        // -------------------------------------------------
        // 1. Validar que venga la inscripción
        // -------------------------------------------------
        if (nota.getEstudianteSeccion() == null
                || nota.getEstudianteSeccion().getId() == null) {

            throw new IllegalArgumentException(
                    "Debe indicar la inscripción del estudiante.");
        }

        Long idInscripcion
                = nota.getEstudianteSeccion().getId();

        // -------------------------------------------------
        // 2. Buscar la inscripción real en la BD
        // -------------------------------------------------
        EstudianteSeccion inscripcion
                = estudianteSeccionRepository
                        .findById(idInscripcion)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "La inscripción no existe."));

        // Usamos el objeto administrado por JPA
        nota.setEstudianteSeccion(inscripcion);

        int totalNotas
                = inscripcion.getSeccion().getTotalNotas();

        // -------------------------------------------------
        // 3. Validar numeroNota
        // -------------------------------------------------
        if (nota.getNumeroNota() == null
                || nota.getNumeroNota() < 1
                || nota.getNumeroNota() > totalNotas) {

            throw new IllegalArgumentException(
                    "El número de nota debe estar entre 1 y "
                    + totalNotas + ".");
        }

        // -------------------------------------------------
        // 4. Evitar número de nota duplicado
        // -------------------------------------------------
        boolean existe
                = notaRepository
                        .existsByEstudianteSeccionIdAndNumeroNota(
                                idInscripcion,
                                nota.getNumeroNota());

        if (existe) {

            throw new IllegalArgumentException(
                    "La nota número "
                    + nota.getNumeroNota()
                    + " ya está registrada.");
        }

        // -------------------------------------------------
        // 5. Validar calificación
        // -------------------------------------------------
        if (nota.getNota() == null
                || nota.getNota().compareTo(
                        BigDecimal.ONE) < 0
                || nota.getNota().compareTo(
                        new BigDecimal("7.0")) > 0) {

            throw new IllegalArgumentException(
                    "La nota debe estar entre 1.0 y 7.0.");
        }

        // -------------------------------------------------
        // 6. Validar ponderación
        // -------------------------------------------------
        if (nota.getPonderacion() == null
                || nota.getPonderacion() <= 0
                || nota.getPonderacion() > 100) {

            throw new IllegalArgumentException(
                    "La ponderación debe estar entre 1 y 100.");
        }

        // -------------------------------------------------
        // 7. Revisar ponderaciones existentes
        // -------------------------------------------------
        List<Nota> notasActuales
                = notaRepository
                        .findByEstudianteSeccionIdOrderByNumeroNotaAsc(
                                idInscripcion);

        int ponderacionActual
                = notasActuales.stream()
                        .mapToInt(Nota::getPonderacion)
                        .sum();

        if (ponderacionActual
                + nota.getPonderacion() > 100) {

            throw new IllegalArgumentException(
                    "La suma de ponderaciones no puede superar 100%.");
        }

        // -------------------------------------------------
        // 8. Guardar nota
        // -------------------------------------------------
        Nota notaGuardada
                = notaRepository.save(nota);

        // -------------------------------------------------
        // 9. Recuperar todas las notas
        // -------------------------------------------------
        List<Nota> todasLasNotas
                = notaRepository
                        .findByEstudianteSeccionIdOrderByNumeroNotaAsc(
                                idInscripcion);

        // -------------------------------------------------
        // 10. ¿Se completaron todas las notas?
        // -------------------------------------------------
        if (todasLasNotas.size() == totalNotas) {

            calcularPromedio(
                    inscripcion,
                    todasLasNotas);
        }

        return notaGuardada;
    }

    private void calcularPromedio(
            EstudianteSeccion inscripcion,
            List<Nota> notas) {

        int totalPonderacion
                = notas.stream()
                        .mapToInt(Nota::getPonderacion)
                        .sum();

        if (totalPonderacion != 100) {

            throw new IllegalArgumentException(
                    "Al completar todas las notas, "
                    + "las ponderaciones deben sumar 100%.");
        }

        BigDecimal suma
                = BigDecimal.ZERO;

        for (Nota nota : notas) {

            BigDecimal ponderacion
                    = BigDecimal.valueOf(
                            nota.getPonderacion())
                            .divide(
                                    BigDecimal.valueOf(100));

            BigDecimal valorPonderado
                    = nota.getNota()
                            .multiply(ponderacion);

            suma
                    = suma.add(valorPonderado);
        }

        BigDecimal promedio
                = suma.setScale(
                        2,
                        RoundingMode.HALF_UP);

        inscripcion.setPromedio(promedio);

        estudianteSeccionRepository.save(inscripcion);
    }

    @Transactional
    public void registrarNotasSeccion(
            Long idSeccion,
            NotaLoteDTO lote) {

        // =========================================
        // 1. Validar el lote
        // =========================================
        if (lote == null
                || lote.getCalificaciones() == null
                || lote.getCalificaciones().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe ingresar al menos una calificación.");
        }

        // =========================================
        // 2. Obtener todos los inscritos
        // =========================================
        List<EstudianteSeccion> inscritos
                = estudianteSeccionRepository
                        .findBySeccionId(idSeccion);

        if (inscritos.isEmpty()) {

            throw new IllegalArgumentException(
                    "La sección no tiene estudiantes inscritos.");
        }

        // =========================================
        // 3. Obtener los ID reales de la sección
        // =========================================
        Set<Long> idsInscritos
                = inscritos.stream()
                        .map(EstudianteSeccion::getId)
                        .collect(Collectors.toSet());

        // =========================================
        // 4. Obtener los ID recibidos
        // =========================================
        if (lote.getCalificaciones()
                .stream()
                .anyMatch(c -> c == null
                || c.getIdEstudianteSeccion() == null)) {

            throw new IllegalArgumentException(
                    "Todas las calificaciones deben indicar una inscripción.");
        }

        Set<Long> idsRecibidos
                = lote.getCalificaciones()
                        .stream()
                        .map(CalificacionDTO::getIdEstudianteSeccion)
                        .collect(Collectors.toSet());

        // =========================================
        // 5. Comprobar duplicados
        // =========================================
        if (idsRecibidos.size()
                != lote.getCalificaciones().size()) {

            throw new IllegalArgumentException(
                    "El lote contiene estudiantes duplicados.");
        }

        // =========================================
        // 6. Comprobar que estén todos
        // =========================================
        if (!idsRecibidos.equals(idsInscritos)) {

            throw new IllegalArgumentException(
                    "Debe ingresar exactamente una calificación "
                    + "por cada estudiante inscrito en la sección.");
        }

        // =========================================
        // 7. Registrar las calificaciones
        // =========================================
        for (CalificacionDTO calificacion
                : lote.getCalificaciones()) {

            EstudianteSeccion inscripcion
                    = estudianteSeccionRepository
                            .findById(
                                    calificacion
                                            .getIdEstudianteSeccion())
                            .orElseThrow(()
                                    -> new IllegalArgumentException(
                                    "La inscripción no existe."));

            Nota nota = new Nota();

            nota.setEstudianteSeccion(inscripcion);
            nota.setNumeroNota(lote.getNumeroNota());
            nota.setTitulo(lote.getTitulo());
            nota.setFecha(lote.getFecha());
            nota.setPonderacion(lote.getPonderacion());
            nota.setNota(calificacion.getNota());

            // Conservamos todas las validaciones existentes
            registrarNota(nota);
        }
    }
}
