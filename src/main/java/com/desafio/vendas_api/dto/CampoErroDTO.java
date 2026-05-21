package com.desafio.vendas_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampoErroDTO {
    private String campo;
    private String mensagem;
}