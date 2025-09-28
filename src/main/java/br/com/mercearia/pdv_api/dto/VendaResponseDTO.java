package br.com.mercearia.pdv_api.dto;

import br.com.mercearia.pdv_api.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record VendaResponseDTO(
        Long id,
        LocalDateTime dataHoraVenda,
        BigDecimal valorTotal,
        Long usuarioId,
        String emailUsuario, // Adicionado para facilitar a visualização
        List<ItemVendaResponseDTO> itens
) {
    public VendaResponseDTO(Venda venda) {
        this(
                venda.getId(),
                venda.getDataHoraVenda(),
                venda.getValorTotal(),
                venda.getUsuario().getId(),
                venda.getUsuario().getEmail(), // Pega o email do usuário
                venda.getItens().stream()
                        .map(ItemVendaResponseDTO::new) // Converte cada ItemVenda para ItemVendaResponseDTO
                        .collect(Collectors.toList())
        );
    }
}