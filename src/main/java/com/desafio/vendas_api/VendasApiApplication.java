package com.desafio.vendas_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")

public class VendasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendasApiApplication.class, args);
    }
}