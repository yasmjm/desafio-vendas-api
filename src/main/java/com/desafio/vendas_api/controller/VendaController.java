package com.desafio.vendas_api.controller;

import com.desafio.vendas_api.dto.VendaRequestDTO;
import com.desafio.vendas_api.dto.VendaResponseDTO;
import com.desafio.vendas_api.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponseDTO> salvarOuAtualizar(
            @Valid @RequestBody VendaRequestDTO dto) {
        
        VendaResponseDTO response = vendaService.salvarOuAtualizar(dto);

       
        return ResponseEntity.ok(response);
    }
}