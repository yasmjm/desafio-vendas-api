package com.desafio.vendas_api.service;

import com.desafio.vendas_api.dto.*;
import com.desafio.vendas_api.entity.Venda;
import com.desafio.vendas_api.entity.VendaItem;
import com.desafio.vendas_api.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    @Transactional
    public VendaResponseDTO salvarOuAtualizar(VendaRequestDTO dto) {
        Venda venda;

        if (dto.getId() != null) {
            venda = vendaRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Venda não encontrada com id: " + dto.getId()));
            sincronizarItens(venda, dto.getItens());
        } else {
            venda = new Venda();
            adicionarItens(venda, dto.getItens());
        }

        venda.setCliente(dto.getCliente());
        venda.setDataVenda(dto.getDataVenda());
        venda.setValorTotal(dto.getValorTotal());
        venda.setStatus(dto.getStatus());
        venda.setFormaPagamento(dto.getFormaPagamento());
        venda.setObservacao(dto.getObservacao());

        Venda salva = vendaRepository.save(venda);

        return converterParaResponseDTO(salva);
    }

    private void sincronizarItens(Venda venda, List<VendaItemRequestDTO> itensDTO) {
        Map<Long, VendaItem> existentes = venda.getItens().stream()
            .collect(Collectors.toMap(VendaItem::getId, item -> item));

        Set<Long> idsMantidos = itensDTO.stream()
            .map(VendaItemRequestDTO::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        venda.getItens().removeIf(item -> !idsMantidos.contains(item.getId()));

        for (VendaItemRequestDTO itemDTO : itensDTO) {
            if (itemDTO.getId() != null && existentes.containsKey(itemDTO.getId())) {
                VendaItem itemExistente = existentes.get(itemDTO.getId());
                mapearCamposItem(itemDTO, itemExistente, venda);
            } else {
                VendaItem novoItem = new VendaItem();
                mapearCamposItem(itemDTO, novoItem, venda);
                venda.getItens().add(novoItem);
            }
        }
    }

    private void adicionarItens(Venda venda, List<VendaItemRequestDTO> itensDTO) {
        for (VendaItemRequestDTO itemDTO : itensDTO) {
            VendaItem item = new VendaItem();
            mapearCamposItem(itemDTO, item, venda);
            venda.getItens().add(item);
        }
    }

    private void mapearCamposItem(VendaItemRequestDTO dto, VendaItem item, Venda venda) {
        item.setProdutoId(dto.getProdutoId());
        item.setDescricaoProduto(dto.getDescricaoProduto());
        item.setQuantidade(dto.getQuantidade());
        item.setValorUnitario(dto.getValorUnitario());
        item.setValorTotal(dto.getValorTotal());
        item.setVenda(venda);
    }

    private VendaResponseDTO converterParaResponseDTO(Venda venda) {
        VendaResponseDTO response = new VendaResponseDTO();
        response.setId(venda.getId());
        response.setCliente(venda.getCliente());
        response.setDataVenda(venda.getDataVenda());
        response.setValorTotal(venda.getValorTotal());
        response.setStatus(venda.getStatus());
        response.setFormaPagamento(venda.getFormaPagamento());
        response.setObservacao(venda.getObservacao());
        response.setItens(
            venda.getItens().stream()
                .map(this::converterItemParaResponseDTO)
                .toList()
        );
        return response;
    }

    private VendaItemResponseDTO converterItemParaResponseDTO(VendaItem item) {
        VendaItemResponseDTO dto = new VendaItemResponseDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProdutoId());
        dto.setDescricaoProduto(item.getDescricaoProduto());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorUnitario(item.getValorUnitario());
        dto.setValorTotal(item.getValorTotal());
        return dto;
    }
}