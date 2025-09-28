package br.com.mercearia.pdv_api.controller;

import br.com.mercearia.pdv_api.dto.ProdutoRequestDTO;
import br.com.mercearia.pdv_api.dto.ProdutoResponseDTO;
import br.com.mercearia.pdv_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import para proteger endpoints
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas usuários com ROLE_ADMIN podem criar produtos
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        ProdutoResponseDTO produto = produtoService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')") // Admin ou Vendedor podem listar
    public ResponseEntity<Page<ProdutoResponseDTO>> listarProdutos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<ProdutoResponseDTO> produtos = produtoService.listarTodosProdutos(pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorCodigo(@PathVariable String codigo) {
        return produtoService.buscarProdutoPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode atualizar
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoService.atualizarProduto(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode desativar
    public ResponseEntity<Void> desativarProduto(@PathVariable Long id) {
        if (produtoService.desativarProduto(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode ativar
    public ResponseEntity<Void> ativarProduto(@PathVariable Long id) {
        if (produtoService.ativarProduto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode deletar
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        if (produtoService.deletarProduto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}