package cl.universidadti.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.universidadti.model.Estudiante;
import cl.universidadti.model.Usuario;
import cl.universidadti.repository.EstudianteRepository;
import cl.universidadti.repository.UsuarioRepository;

/**
 *
 * DatosInicialesConfig
 *
 * @author UniversidadTI
 * @version 1.0
 */
@Configuration
public class DatosInicialesConfig {

    /**
     * Crea usuarios iniciales para la aplicación
     *
     * @param usuarioRepository
     * @param estudianteRepository
     * @param passwordEncoder
     * @return
     */
    @Bean
    CommandLineRunner crearUsuariosIniciales(
            UsuarioRepository usuarioRepository,
            EstudianteRepository estudianteRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!usuarioRepository.existsByEmail(
                    "admin@universidad.cl")) {

                Usuario admin = new Usuario();

                admin.setEmail(
                        "admin@universidad.cl");

                admin.setPassword(
                        passwordEncoder.encode("123456"));

                admin.setRol("ADMIN");
                admin.setHabilitado(true);

                usuarioRepository.save(admin);
            }

            if (!usuarioRepository.existsByEmail(
                    "estudiante@universidad.cl")) {

                Usuario estudiante = new Usuario();

                estudiante.setEmail(
                        "estudiante@universidad.cl");

                estudiante.setPassword(
                        passwordEncoder.encode("123456"));

                estudiante.setRol("ESTUDIANTE");
                estudiante.setHabilitado(true);

                usuarioRepository.save(estudiante);
            }
            // Crear cuentas para estudiantes antiguos

            for (Estudiante estudiante
                    : estudianteRepository.findAll()) {

                if (!usuarioRepository.existsByEmail(
                        estudiante.getEmail())) {

                    Usuario usuario = new Usuario();

                    usuario.setEmail(
                            estudiante.getEmail());

                    usuario.setPassword(
                            passwordEncoder.encode("123456"));

                    usuario.setRol("ESTUDIANTE");

                    usuario.setHabilitado(
                            Boolean.TRUE.equals(
                                    estudiante.getVigente()));

                    usuarioRepository.save(usuario);

                    System.out.println(
                            "Usuario creado para: "
                            + estudiante.getEmail());
                }
            }

        };
    }

}
