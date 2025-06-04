package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Зашифрованный номер карты
     */
    private String encodedNumber;
    /**
     * Владелец карты
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    /**
     * Время окончания срока действия карты
     */
    private LocalDateTime expiredTime;
    /**
     * Статус активности карты
     */
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    /**
     * Баланс карты
     */
    @Column(name = "balance", precision = 20, scale = 2)
    private BigDecimal balance;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Card card = (Card) object;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
