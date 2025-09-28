package br.com.mercearia.pdv_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record VendaRequestDTO(
        @NotEmpty(message = "A venda deve conter pelo menos um item")
        List<@Valid ItemVendaRequestDTO> itens
) {
}