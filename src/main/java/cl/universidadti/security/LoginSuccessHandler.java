package cl.universidadti.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Redirige al portal correspondiente después de una autenticación exitosa.
 */
@Component
public class LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    /**
     * Redirige a administración o al portal del estudiante según el rol.
     *
     * @param request solicitud HTTP original
     * @param response respuesta HTTP donde se establece la redirección
     * @param authentication identidad autenticada y sus autoridades
     * @throws IOException si no se puede enviar la redirección
     * @throws ServletException si falla el procesamiento del servlet
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        boolean esAdmin
                = authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(a
                                -> a.getAuthority()
                                .equals("ROLE_ADMIN"));

        if (esAdmin) {

            response.sendRedirect("/admin");

        } else {

            response.sendRedirect("/estudiante");
        }
    }
}
