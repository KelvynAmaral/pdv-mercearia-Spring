package br.com.mercearia.pdv_api.service;

import br.com.mercearia.pdv_api.dto.ProdutoRequestDTO;
import br.com.mercearia.pdv_api.dto.ProdutoResponseDTO;
import br.com.mercearia.pdv_api.model.Produto;
import br.com.mercearia.pdv_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        // Verifica se já existe um produto com o mesmo código
        if (produtoRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new RuntimeException("Já existe um produto com o código: " + dto.codigo());
        }

        Produto produto = new Produto(
                dto.codigo(),
                dto.nome(),
                dto.descricao(),
                dto.precoVenda(),
                dto.quantidadeEstoque()
        );
        produto.setAtivo(true); // Garante que novos produtos são ativos por padrão
        Produto produtoSalvo = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produtoSalvo);
    }

    public Page<ProdutoResponseDTO> listarTodosProdutos(Pageable pageable) {
        Page<Produto> produtosPage = produtoRepository.findAll(pageable);
        return produtosPage.map(ProdutoResponseDTO::new);
    }

    public Optional<ProdutoResponseDTO> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id).map(ProdutoResponseDTO::new);
    }

    public Optional<ProdutoResponseDTO> buscarProdutoPorCodigo(String codigo) {
        return produtoRepository.findByCodigo(codigo).map(ProdutoResponseDTO::new);
    }

    @Transactional
    public Optional<ProdutoResponseDTO> atualizarProduto(Long id, ProdutoRequestDTO dto) {
        return produtoRepository.findById(id).map(produto -> {
            // Verifica se o código está sendo alterado para um código já existente
            if (!produto.getCodigo().equals(dto.codigo()) && produtoRepository.findByCodigo(dto.codigo()).isPresent()) {
                throw new RuntimeException("Já existe outro produto com o código: " + dto.codigo());
            }

            produto.setCodigo(dto.codigo());
            produto.setNome(dto.nome());
            produto.setDescricao(dto.descricao());
            produto.setPrecoVenda(dto.precoVenda());
            produto.setQuantidadeEstoque(dto.quantidadeEstoque());
            Produto produtoAtualizado = produtoRepository.save(produto);
            return new ProdutoResponseDTO(produtoAtualizado);
        });
    }

    @Transactional
    public boolean desativarProduto(Long id) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setAtivo(false); // Apenas desativa, não exclui fisicamente
            produtoRepository.save(produto);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean ativarProduto(Long id) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setAtivo(true);
            produtoRepository.save(produto);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deletarProduto(Long id) {
        // Em muitos sistemas, é preferível desativar do que deletar fisicamente.
        // Aqui, faremos a exclusão física, mas tenha isso em mente para sistemas reais.
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}