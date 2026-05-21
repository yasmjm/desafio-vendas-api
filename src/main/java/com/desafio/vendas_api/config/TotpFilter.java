package com.desafio.vendas_api.config;

import com.desafio.vendas_api.service.TotpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class TotpFilter extends OncePerRequestFilter {

    private final TotpService totpService;
    private final ObjectMapper objectMapper;

    public TotpFilter(TotpService totpService, ObjectMapper objectMapper) {
        this.totpService = totpService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean usuarioAutenticado = auth != null
            && auth.isAuthenticated()
            && !"anonymousUser".equals(auth.getPrincipal());

        if (usuarioAutenticado) {
            String otpCode = request.getHeader("X-OTP-Code");

            if (!totpService.validar(otpCode)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), Map.of(
                    "status", 401,
                    "erro", "Código 2FA inválido",
                    "mensagem", "O header X-OTP-Code está ausente ou expirou."
                ));
                return; 
            }
        }

        filterChain.doFilter(request, response);
    }
}