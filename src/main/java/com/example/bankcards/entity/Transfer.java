package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Карта-источник
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_card_id", referencedColumnName = "id")
    private Card sourceCard;
    /**
     * Карта-получатель
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_card_id", referencedColumnName = "id")
    private Card targetCard;
    /**
     * Сумма перевода
     */
    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;
}
