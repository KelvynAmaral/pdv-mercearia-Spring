package br.com.mercearia.pdv_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
@Data // Lombok: gera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: gera construtor sem argumentos
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp // Preenche automaticamente com a data/hora da criação
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHoraVenda;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    // Muitos itens de venda para uma única venda
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    // Muitas vendas para um único usuário (vendedor)
    @ManyToOne(fetch = FetchType.LAZY) // Carregamento sob demanda
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // O vendedor que realizou a venda

    public Venda(Usuario usuario) {
        this.usuario = usuario;
        this.valorTotal = BigDecimal.ZERO; // Inicializa o total como zero
    }

    // Método para adicionar item e recalcular o total
    public void addItemVenda(ItemVenda item) {
        if (item.getVenda() != this) { // Garante o link bidirecional
            item.setVenda(this);
        }
        this.itens.add(item);
        recalcularValorTotal();
    }

    // Método para recalcular o valor total da venda
    public void recalcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(ItemVenda::getValorTotalItem) // Pega o total de cada item
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma tudo
    }
}