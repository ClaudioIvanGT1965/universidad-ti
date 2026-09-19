package cl.universidadti.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.universidadti.model.Usuario;
import cl.universidadti.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

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
