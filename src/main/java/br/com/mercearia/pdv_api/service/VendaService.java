package br.com.mercearia.pdv_api.service;

import br.com.mercearia.pdv_api.dto.VendaRequestDTO;
import br.com.mercearia.pdv_api.dto.VendaResponseDTO;
import br.com.mercearia.pdv_api.model.ItemVenda;
import br.com.mercearia.pdv_api.model.Produto;
import br.com.mercearia.pdv_api.model.Usuario;
import br.com.mercearia.pdv_api.model.Venda;
import br.com.mercearia.pdv_api.repository.ProdutoRepository;
import br.com.mercearia.pdv_api.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional // Garante que toda a operação seja atômica (tudo ou nada)
    public VendaResponseDTO criarVenda(VendaRequestDTO dto) {
        // 1. Pega o usuário autenticado no momento da requisição
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Cria a entidade Venda, associando ao usuário
        Venda venda = new Venda(usuario);

        // 3. Itera sobre cada item do DTO de requisição
        dto.itens().forEach(itemDTO -> {
            // Busca o produto no banco de dados
            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + itemDTO.produtoId()));

            // Verifica se há estoque suficiente
            if (produto.getQuantidadeEstoque() < itemDTO.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // Dá baixa no estoque
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDTO.quantidade());

            // Cria o ItemVenda e o associa à Venda
            ItemVenda itemVenda = new ItemVenda(venda, produto, itemDTO.quantidade(), produto.getPrecoVenda());
            venda.addItemVenda(itemVenda); // Adiciona o item à lista da venda
        });

        // 4. Salva a venda e todos os seus itens (graças ao CascadeType.ALL)
        Venda vendaSalva = vendaRepository.save(venda);

        // 5. Converte a entidade salva para um DTO de resposta e retorna
        return new VendaResponseDTO(vendaSalva);
    }

    @Transactional(readOnly = true) // Transação apenas de leitura, mais otimizada
    public List<VendaResponseDTO> listarTodasVendas() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendaResponseDTO buscarVendaPorId(Long id) {
        return vendaRepository.findById(id)
                .map(VendaResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com o ID: " + id));
    }
}