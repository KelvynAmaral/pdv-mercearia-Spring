package br.com.mercearia.pdv_api.controller;

import br.com.mercearia.pdv_api.dto.VendaRequestDTO;
import br.com.mercearia.pdv_api.dto.VendaResponseDTO;
import br.com.mercearia.pdv_api.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<VendaResponseDTO> criarVenda(@RequestBody @Valid VendaRequestDTO dto) {
        VendaResponseDTO venda = vendaService.criarVenda(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode ver o histórico de todas as vendas
    public ResponseEntity<List<VendaResponseDTO>> listarVendas() {
        List<VendaResponseDTO> vendas = vendaService.listarTodasVendas();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendaResponseDTO> buscarVendaPorId(@PathVariable Long id) {
        VendaResponseDTO venda = vendaService.buscarVendaPorId(id);
        return ResponseEntity.ok(venda);
    }
}