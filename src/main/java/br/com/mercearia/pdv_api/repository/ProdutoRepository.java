package br.com.mercearia.pdv_api.repository;

import br.com.mercearia.pdv_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(String codigo); // Para buscar um produto pelo código

}