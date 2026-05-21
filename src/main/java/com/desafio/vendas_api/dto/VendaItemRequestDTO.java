package com.desafio.vendas_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class VendaItemRequestDTO {
    private Long id;

    @NotNull(message = "O produto é obrigatório.")
    private Long produtoId;

    @NotBlank(message = "A descrição do produto é obrigatória.")
    private String descricaoProduto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;

    @NotNull(message = "O valor unitário é obrigatório.")
    @Positive(message = "O valor unitário deve ser maior que zero.")
    private BigDecimal valorUnitario;

    @NotNull(message = "O valor total do item é obrigatório.")
    @Positive(message = "O valor total do item deve ser maior que zero.")
    private BigDecimal valorTotal;
}