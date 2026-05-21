package com.desafio.vendas_api.controller;

import com.desafio.vendas_api.service.TotpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final TotpService totpService;

    @GetMapping("/atual")
    public ResponseEntity<Map<String, String>> obterCodigoAtual() {
        return ResponseEntity.ok(Map.of(
            "codigo", totpService.gerarCodigoAtual(),
            "instrucao", "Use este código no header X-OTP-Code. Válido por 30 segundos.",
            "aviso", "Este endpoint existe apenas para fins de demonstração."
        ));
    }
}