package com.desafio.vendas_api.repository;

import com.desafio.vendas_api.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // diz ao Spring que este é um componente de acesso a dados
public interface VendaRepository extends JpaRepository<Venda, Long> {
}