package com.iflower.security;

import com.iflower.model.Usuario;
import com.iflower.model.enums.Papel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoSegundos;

    public JwtService(@Value("${iflower.jwt.secret}") String segredo,
                      @Value("${iflower.jwt.expiracao-segundos:7200}") long expiracaoSegundos) {
        this.chave = Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8));
        this.expiracaoSegundos = expiracaoSegundos;
    }

    public String gerar(Usuario usuario) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(usuario.getId()))
                .claim("email", usuario.getEmail())
                .claim("papel", usuario.getPapel().name())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(expiracaoSegundos)))
                .signWith(chave)
                .compact();
    }

    public Optional<UsuarioAutenticado> validar(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(chave).build().parseSignedClaims(token).getPayload();
            return Optional.of(new UsuarioAutenticado(
                    Long.valueOf(claims.getSubject()),
                    claims.get("email", String.class),
                    Papel.valueOf(claims.get("papel", String.class))));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public long getExpiracaoSegundos() {
        return expiracaoSegundos;
    }
}
