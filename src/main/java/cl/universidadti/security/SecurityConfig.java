package cl.universidadti.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // =====================================
                // CSRF
                // =====================================
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                )
                // =====================================
                // AUTORIZACIÓN
                // =====================================
                .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers(
                        "/login",
                        "/css/**",
                        "/js/**",
                        "/img/**"
                ).permitAll()
                // Portales web
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .requestMatchers("/estudiante/**")
                .hasRole("ESTUDIANTE")
                // API personal del estudiante
                .requestMatchers("/api/mi-cuenta/**")
                .hasRole("ESTUDIANTE")
                // API administrativas
                .requestMatchers("/api/estudiantes/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/cursos/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/secciones/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/inscripciones/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/notas/**")
                .hasRole("ADMIN")
                // Otras API
                .requestMatchers("/api/**")
                .authenticated()
                .anyRequest().permitAll()
                )
                // =====================================
                // LOGIN PERSONALIZADO
                // =====================================
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .permitAll()
                )
                // =====================================
                // AUTENTICACIÓN PARA CURL
                // =====================================
                .httpBasic(basic -> {
                })
                // =====================================
                // CIERRE DE SESIÓN
                // =====================================
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private final LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(
            LoginSuccessHandler loginSuccessHandler) {

        this.loginSuccessHandler
                = loginSuccessHandler;
    }

}
