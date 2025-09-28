package br.com.mercearia.pdv_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario implements UserDetails { // Implementa UserDetails para o Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // Usaremos o email como login (ou um username, se preferir)

    @Column(nullable = false)
    private String senha;

    // Relacionamento muitos-para-muitos com Role
    @ManyToMany(fetch = FetchType.EAGER) // Carrega as roles junto com o usuário
    @JoinTable(
            name = "usuario_roles", // Tabela de junção
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Usuario(String email, String senha, Set<Role> roles) {
        this.email = email;
        this.senha = senha;
        this.roles = roles;
    }

    // --- Métodos da interface UserDetails (para o Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte as roles para uma coleção de GrantedAuthority
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getNome()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Conta sempre ativa
    }
}
