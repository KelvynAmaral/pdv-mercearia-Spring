package br.com.mercearia.pdv_api.repository;

import br.com.mercearia.pdv_api.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    //Buscar vendas por um usuário específico
    List<Venda> findByUsuarioId(Long usuarioId);

    //  Buscar vendas em um período de tempo
    List<Venda> findByDataHoraVendaBetween(LocalDateTime inicio, LocalDateTime fim);
}