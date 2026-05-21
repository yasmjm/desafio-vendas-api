package com.desafio.vendas_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VendaResponseDTO {
    private Long id;
    private String cliente;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
    private String status;
    private String formaPagamento;
    private String observacao;
    private List<VendaItemResponseDTO> itens;
}