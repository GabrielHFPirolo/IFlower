package com.iflower.service;

import com.iflower.dto.AuthDTO;
import com.iflower.dto.UsuarioDTO;
import com.iflower.exception.NaoAutenticadoException;
import com.iflower.model.Usuario;
import com.iflower.repository.UsuarioRepository;
import com.iflower.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RF002 - login. */
@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final String MENSAGEM = "Email ou senha inválidos.";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest req) {
        // Mesma mensagem para "email inexistente" e "senha errada": não revela quais emails existem.
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(req.email().trim())
                .filter(Usuario::isAtivo)
                .filter(u -> passwordEncoder.matches(req.senha(), u.getSenhaHash()))
                .orElseThrow(() -> new NaoAutenticadoException(MENSAGEM));
        return new AuthDTO.LoginResponse(jwtService.gerar(usuario), "Bearer",
                jwtService.getExpiracaoSegundos(), UsuarioDTO.Response.from(usuario));
    }
}
