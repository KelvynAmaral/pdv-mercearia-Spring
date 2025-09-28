package br.com.mercearia.pdv_api.dto;

import br.com.mercearia.pdv_api.model.ItemVenda;

import java.math.BigDecimal;

public record ItemVendaResponseDTO(
        Long id,
        Long produtoId,
        String nomeProduto, // Adicionado para facilitar a visualização
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotalItem
) {
    public ItemVendaResponseDTO(ItemVenda itemVenda) {
        this(
                itemVenda.getId(),
                itemVenda.getProduto().getId(),
                itemVenda.getProduto().getNome(), // Pega o nome do produto
                itemVenda.getQuantidade(),
                itemVenda.getPrecoUnitario(),
                itemVenda.getValorTotalItem()
        );
    }
}