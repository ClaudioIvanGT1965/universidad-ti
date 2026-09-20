package cl.universidadti.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.universidadti.model.Usuario;
import cl.universidadti.repository.UsuarioRepository;

/**
 * Adapta las cuentas persistidas al modelo de autenticación de Spring Security.
 */
@Service
public class UsuarioDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el servicio de carga de usuarios.
     *
     * @param usuarioRepository repositorio de cuentas
     */
    public UsuarioDetailsService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga una cuenta por su correo electrónico.
     *
     * @param email correo utilizado como nombre de usuario
     * @return detalles de seguridad de la cuenta
     * @throws UsernameNotFoundException si no existe la cuenta
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario
                = usuarioRepository
                        .findByEmail(email)
                        .orElseThrow(()
                                -> new UsernameNotFoundException(
                                "Usuario no encontrado: " + email));

        return User
                .withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .disabled(!Boolean.TRUE.equals(
                        usuario.getHabilitado()))
                .build();
    }
}
