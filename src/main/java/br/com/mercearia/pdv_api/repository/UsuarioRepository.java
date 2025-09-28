package br.com.mercearia.pdv_api.repository;

import br.com.mercearia.pdv_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email); // Para buscar um usuário pelo email
}