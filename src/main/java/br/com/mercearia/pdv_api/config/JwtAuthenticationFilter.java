package br.com.mercearia.pdv_api.config;

import br.com.mercearia.pdv_api.repository.UsuarioRepository;
import br.com.mercearia.pdv_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um componente gerenciado pelo Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Recupera o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        // 2. Se um token foi encontrado...
        if (token != null) {
            // 3. Valida o token e pega o email (subject) de dentro dele
            var subject = tokenService.validateToken(token);

            // 4. Busca o usuário no banco de dados com base no email do token
            UserDetails user = usuarioRepository.findByEmail(subject).orElse(null);

            // 5. Se o usuário existir, cria uma "autenticação" e a coloca no contexto de segurança do Spring
            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua a cadeia de filtros do Spring
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        // Remove "Bearer " para obter apenas a string do token
        return authHeader.replace("Bearer ", "");
    }
}