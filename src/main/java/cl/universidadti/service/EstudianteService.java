package cl.universidadti.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.universidadti.model.Estudiante;
import cl.universidadti.model.EstudianteSeccion;
import cl.universidadti.model.Usuario;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.EstudianteSeccionRepository;
import cl.universidadti.repository.UsuarioRepository;

/**
 * Servicio que gestiona estudiantes y sus cuentas de acceso.
 */
@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteSeccionRepository estudianteSeccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea el servicio de estudiantes.
     *
     * @param estudianteRepository repositorio de estudiantes
     * @param estudianteSeccionRepository repositorio de inscripciones
     * @param usuarioRepository repositorio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public EstudianteService(
            EstudianteRepository estudianteRepository,
            EstudianteSeccionRepository estudianteSeccionRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.estudianteRepository = estudianteRepository;
        this.estudianteSeccionRepository
                = estudianteSeccionRepository;

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Valida y crea un estudiante junto con su cuenta de acceso inicial.
     *
     * @param estudiante datos del estudiante
     * @return estudiante persistido
     * @throws IllegalArgumentException si faltan datos o ya existe el correo
     */
    @Transactional
    public Estudiante crear(Estudiante estudiante) {

        // 1. Validar nombre
        if (estudiante.getNombre() == null
                || estudiante.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre del estudiante es obligatorio.");
        }

        // 2. Validar email
        if (estudiante.getEmail() == null
                || estudiante.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "El email del estudiante es obligatorio.");
        }

        // 3. Verificar estudiante duplicado
        if (estudianteRepository.existsByEmail(
                estudiante.getEmail())) {

            throw new IllegalArgumentException(
                    "Ya existe un estudiante con ese email.");
        }

        // 4. Verificar usuario duplicado
        if (usuarioRepository.existsByEmail(
                estudiante.getEmail())) {

            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese email.");
        }

        // 5. Preparar estudiante
        estudiante.setId(null);
        estudiante.setFechaIngreso(LocalDate.now());
        estudiante.setVigente(true);

        // 6. Guardar estudiante
        Estudiante estudianteGuardado
                = estudianteRepository.save(estudiante);

        // 7. Crear cuenta de usuario
        Usuario usuario = new Usuario();

        usuario.setEmail(estudianteGuardado.getEmail());

        usuario.setPassword(
                passwordEncoder.encode("123456"));

        usuario.setRol("ESTUDIANTE");
        usuario.setHabilitado(true);

        // 8. Guardar usuario
        usuarioRepository.save(usuario);

        // 9. Retornar estudiante
        return estudianteGuardado;
    }

    /**
     * Desactiva al estudiante, su cuenta y sus inscripciones.
     *
     * @param idEstudiante identificador del estudiante
     * @throws IllegalArgumentException si el estudiante o su cuenta no existe
     */
    @Transactional
    public void eliminarLogicamente(Long idEstudiante) {

        // 1. Buscar estudiante
        Estudiante estudiante
                = estudianteRepository
                        .findById(idEstudiante)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "El estudiante no existe."));

        // 2. Verificar estado
        if (!Boolean.TRUE.equals(estudiante.getVigente())) {

            throw new IllegalArgumentException(
                    "El estudiante ya se encuentra eliminado.");
        }

        // 3. Buscar usuario asociado
        Usuario usuario
                = usuarioRepository
                        .findByEmail(estudiante.getEmail())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "No existe una cuenta de usuario "
                                + "asociada al estudiante."));

        // 4. Buscar inscripciones
        List<EstudianteSeccion> inscripciones
                = estudianteSeccionRepository
                        .findByEstudianteId(idEstudiante);

        // 5. Eliminar inscripciones.
        // Las notas se eliminan por ON DELETE CASCADE.
        estudianteSeccionRepository.deleteAll(inscripciones);

        // 6. Baja académica
        estudiante.setVigente(false);
        estudianteRepository.save(estudiante);

        // 7. Deshabilitar acceso
        usuario.setHabilitado(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos personales y sincroniza el correo de acceso.
     *
     * @param id identificador del estudiante
     * @param datos nuevos datos del estudiante
     * @return estudiante actualizado
     */
    @Transactional
    public Estudiante actualizar(
            Long id,
            Estudiante datos) {

        Estudiante estudiante = estudianteRepository
                .findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Estudiante no encontrado."));

        if (datos.getNombre() == null
                || datos.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre es obligatorio.");
        }

        if (datos.getEmail() == null
                || datos.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "El email es obligatorio.");
        }

        // Si el correo cambió, debemos comprobar
        // que no pertenezca a otro estudiante.
        if (!estudiante.getEmail()
                .equalsIgnoreCase(datos.getEmail())) {

            if (estudianteRepository
                    .existsByEmail(datos.getEmail())) {

                throw new IllegalArgumentException(
                        "El email ya está registrado.");
            }

            Usuario usuario = usuarioRepository
                    .findByEmail(estudiante.getEmail())
                    .orElseThrow(()
                            -> new IllegalArgumentException(
                            "Usuario asociado no encontrado."));

            usuario.setEmail(datos.getEmail());

            usuarioRepository.save(usuario);
        }

        estudiante.setNombre(datos.getNombre());
        estudiante.setEmail(datos.getEmail());

        if (datos.getFechaIngreso() != null) {
            estudiante.setFechaIngreso(
                    datos.getFechaIngreso());
        }

        return estudianteRepository.save(estudiante);
    }

}
