package br.com.mercearia.pdv_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal; // Import para valores monetários precisos

@Entity
@Table(name = "produtos")
@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: gera construtor sem argumentos
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo; // Código de barras ou SKU do produto

    @Column(nullable = false)
    private String nome; // Nome do produto (ex: "Coca-cola Lata")

    private String descricao; // Descrição opcional

    @Column(nullable = false, precision = 10, scale = 2) // Total de 10 dígitos, 2 decimais
    private BigDecimal precoVenda; // Preço de venda (usamos BigDecimal para precisão monetária)

    @Column(nullable = false)
    private Integer quantidadeEstoque; // Quantidade disponível em estoque

    private boolean ativo = true; // Indica se o produto está ativo para venda

    public Produto(String codigo, String nome, String descricao, BigDecimal precoVenda, Integer quantidadeEstoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }
}