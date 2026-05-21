package com.desafio.vendas_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ErroValidacaoResponseDTO {
    private int status;
    private String erro;
    private String mensagem;
    private List<CampoErroDTO> campos;
}