package br.com.mercearia.pdv_api.dto;

import br.com.mercearia.pdv_api.model.Produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String codigo,
        String nome,
        String descricao,
        BigDecimal precoVenda,
        Integer quantidadeEstoque,
        boolean ativo
) {
    // Construtor que recebe uma entidade Produto e a converte para DTO
    public ProdutoResponseDTO(Produto produto) {
        this(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPrecoVenda(),
                produto.getQuantidadeEstoque(),
                produto.isAtivo()
        );
    }
}