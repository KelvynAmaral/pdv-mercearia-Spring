package br.com.mercearia.pdv_api.repository;

import br.com.mercearia.pdv_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNome(String nome); // Para buscar uma role pelo nome (ROLE_ADMIN, ROLE_VENDEDOR)
}
