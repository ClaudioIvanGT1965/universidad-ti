package cl.universidadti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.universidadti.model.Usuario;

/**
 * Repositorio de persistencia para cuentas de usuario.
 */
public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    /**
     * @param email correo de la cuenta
     * @return usuario encontrado, si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * @param email correo de la cuenta
     * @return {@code true} si ya existe una cuenta con ese correo
     */
    boolean existsByEmail(String email);
}
