package br.com.mercearia.pdv_api.repository;

import br.com.mercearia.pdv_api.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    // Exemplo: Buscar itens de venda por uma venda específica
    List<ItemVenda> findByVendaId(Long vendaId);
}