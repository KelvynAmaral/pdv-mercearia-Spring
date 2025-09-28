package br.com.mercearia.pdv_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O código do produto é obrigatório")
        @Size(max = 50, message = "O código não pode exceder 50 caracteres")
        String codigo,

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres")
        String nome,

        String descricao, // Opcional

        @NotNull(message = "O preço de venda é obrigatório")
        @Positive(message = "O preço de venda deve ser um valor positivo")
        BigDecimal precoVenda,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Positive(message = "A quantidade em estoque deve ser um valor positivo ou zero") // Permite 0
        Integer quantidadeEstoque
) {
}