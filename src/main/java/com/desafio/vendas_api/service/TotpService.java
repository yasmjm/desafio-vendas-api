package com.desafio.vendas_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    @Value("${app.totp.secret:DESAFIO_SECRET_VENDAS_2026}")
    private String secret;

    private static final long PASSO_EM_SEGUNDOS = 30;

    public boolean validar(String codigoInformado) {
        if (codigoInformado == null || codigoInformado.isBlank()) {
            return false;
        }

        long passoAtual = System.currentTimeMillis() / 1000 / PASSO_EM_SEGUNDOS;

        for (long passo = passoAtual - 1; passo <= passoAtual; passo++) {
            if (gerarCodigo(passo).equals(codigoInformado)) {
                return true;
            }
        }
        return false;
    }

    public String gerarCodigoAtual() {
        long passoAtual = System.currentTimeMillis() / 1000 / PASSO_EM_SEGUNDOS;
        return gerarCodigo(passoAtual);
    }

    private String gerarCodigo(long passo) {
        int hash = Math.abs((secret + passo).hashCode());
        return String.format("%06d", hash % 1_000_000);
    }
}