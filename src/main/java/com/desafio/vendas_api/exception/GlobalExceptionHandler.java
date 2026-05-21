package com.desafio.vendas_api.exception;

import com.desafio.vendas_api.dto.CampoErroDTO;
import com.desafio.vendas_api.dto.ErroValidacaoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponseDTO> handleValidation(
            MethodArgumentNotValidException ex) {

        List<CampoErroDTO> campos = ex.getBindingResult().getFieldErrors().stream()
            .map(this::toCampoErroDTO)
            .toList();

        ErroValidacaoResponseDTO erro = new ErroValidacaoResponseDTO();
        erro.setStatus(400);
        erro.setErro("Erro de validação");
        erro.setMensagem("Existem campos inválidos na requisição.");
        erro.setCampos(campos);

        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
            "status", 404,
            "erro", "Recurso não encontrado",
            "mensagem", ex.getMessage()
        ));
    }

    private CampoErroDTO toCampoErroDTO(FieldError error) {
        return new CampoErroDTO(error.getField(), error.getDefaultMessage());
    }
}