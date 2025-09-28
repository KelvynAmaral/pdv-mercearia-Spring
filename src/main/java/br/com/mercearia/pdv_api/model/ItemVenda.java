package br.com.mercearia.pdv_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_venda")
@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: gera construtor sem argumentos
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos itens de venda para uma venda
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos itens de venda para um produto
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario; // Preço do produto no momento da venda

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalItem; // Preço unitário * quantidade

    public ItemVenda(Venda venda, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotalItem = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Atualiza o valor total do item se a quantidade ou preço mudarem
    @PrePersist @PreUpdate
    public void calcularValorTotalItem() {
        if (this.precoUnitario != null && this.quantidade != null) {
            this.valorTotalItem = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
    }
}