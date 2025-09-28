package br.com.mercearia.pdv_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemVendaRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
        Integer quantidade
) {
}