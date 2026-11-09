package com.iflower.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflower.exception.ApiError;
import com.iflower.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * RNF005 - senhas com BCrypt e rotas protegidas por JWT.
 * A autorização por perfil (CLIENTE/VENDEDOR) é declarada aqui; a posse do recurso
 * (ex.: o pedido é deste cliente?) é verificada nos services.
 * ATENÇÃO: a ordem das regras importa (a primeira que casar vale).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter, ObjectMapper mapper)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        // públicas
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/clientes", "/api/vendedores").permitAll()
                        // rotas específicas ANTES dos coringas
                        .requestMatchers("/api/lojas/me").hasRole("VENDEDOR")
                        .requestMatchers("/api/produtos/me").hasRole("VENDEDOR")
                        .requestMatchers(HttpMethod.GET, "/api/lojas/proximas").hasRole("CLIENTE")
                        // navegação pública (RF004, RF007)
                        .requestMatchers(HttpMethod.GET, "/api/lojas", "/api/lojas/*", "/api/lojas/*/produtos",
                                "/api/lojas/*/avaliacoes", "/api/produtos/*").permitAll()
                        // vendedor
                        .requestMatchers("/api/produtos/**").hasRole("VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/status").hasRole("VENDEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/pagamentos/*/confirmar").hasRole("VENDEDOR")
                        // cliente
                        .requestMatchers("/api/carrinho/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos", "/api/pedidos/*/cancelar",
                                "/api/pedidos/*/pagamento").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/lojas/*/avaliacoes").hasRole("CLIENTE")
                        .requestMatchers("/api/avaliacoes/**").hasRole("CLIENTE")
                        // demais (ex.: /api/usuarios/me, GET /api/pedidos): qualquer usuário autenticado
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) ->
                                escrever(res, mapper, 401, "Autenticação necessária ou token inválido."))
                        .accessDeniedHandler((req, res, ex) ->
                                escrever(res, mapper, 403, "Você não tem permissão para acessar este recurso.")))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /** CORS liberado para desenvolvimento (app React Native / Expo). Restringir em produção. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    private void escrever(HttpServletResponse res, ObjectMapper mapper, int status, String mensagem)
            throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), new ApiError(Instant.now(), status, mensagem, null));
    }
}
