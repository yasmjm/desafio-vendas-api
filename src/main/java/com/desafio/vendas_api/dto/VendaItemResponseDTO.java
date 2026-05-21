package com.desafio.vendas_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class VendaItemResponseDTO {
    private Long id;
    private Long produtoId;
    private String descricaoProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
}