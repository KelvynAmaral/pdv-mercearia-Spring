package br.com.mercearia.pdv_api.controller;

import br.com.mercearia.pdv_api.dto.LoginRequestDTO;
import br.com.mercearia.pdv_api.dto.LoginResponseDTO;
import br.com.mercearia.pdv_api.dto.RegistroRequestDTO;
import br.com.mercearia.pdv_api.model.Role;
import br.com.mercearia.pdv_api.model.Usuario;
import br.com.mercearia.pdv_api.repository.RoleRepository;
import br.com.mercearia.pdv_api.repository.UsuarioRepository;
import br.com.mercearia.pdv_api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody RegistroRequestDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().build(); // Retorna erro 400 se o usuário já existir
        }

        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Role roleUsuario = roleRepository.findByNome(data.role())
                .orElseThrow(() -> new RuntimeException("Erro: Role não encontrada."));

        Usuario novoUsuario = new Usuario(data.email(), senhaCriptografada, Set.of(roleUsuario));
        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}
