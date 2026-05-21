package com.desafio.vendas_api.repository;

import com.desafio.vendas_api.entity.VendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaItemRepository extends JpaRepository<VendaItem, Long> {
}