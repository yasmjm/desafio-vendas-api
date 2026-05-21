package com.desafio.vendas_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * representa a tabela "venda_item".
 *
 * @ManyToOne: muitos itens pertencem a uma venda.
 * FetchType.LAZY: o objeto Venda nao e carregado do banco automaticamente,
 * so quando for acessado explicitamente (economia de recursos).
 *
 * @JoinColumn: define a coluna de chave estrangeira (venda_id) nesta tabela.
 */
@Entity
@Table(name = "venda_item")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class VendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long produtoId;
    private String descricaoProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id")
    private Venda venda;

    // --- campos-auditoria ---

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    private LocalDateTime alteradoEm;

    @CreatedBy
    @Column(updatable = false)
    private String criadoPor;

    @LastModifiedBy
    private String alteradoPor;
}
