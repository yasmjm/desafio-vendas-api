package com.desafio.vendas_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VendaRequestDTO {
    private Long id;

    @NotBlank(message = "O cliente é obrigatório.")
    private String cliente;

    @NotNull(message = "A data da venda é obrigatória.")
    private LocalDate dataVenda;

    @NotNull(message = "O valor total é obrigatório.")
    @Positive(message = "O valor total deve ser maior que zero.")
    private BigDecimal valorTotal;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @NotBlank(message = "A forma de pagamento é obrigatória.")
    private String formaPagamento;

    private String observacao;

    @NotEmpty(message = "A venda deve possuir pelo menos um item.")
    @Valid
    private List<VendaItemRequestDTO> itens;
}