package br.com.mercearia.pdv_api.service;

import br.com.mercearia.pdv_api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Pega as roles do usuário e as transforma em uma lista de Strings
            var roles = usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return JWT.create()
                    .withIssuer("pdv-api")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(getExpirationDate())
                    // ADICIONA AS PERMISSÕES (ROLES) AO TOKEN
                    .withClaim("roles", roles)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida um token JWT e retorna o subject (email) se for válido.
     * @param token o token JWT
     * @return o email do usuário se o token for válido, ou uma string vazia caso contrário.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("pdv-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return ""; // Retorna vazio se o token for inválido
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}