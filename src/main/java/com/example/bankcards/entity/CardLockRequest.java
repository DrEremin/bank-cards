package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Этот класс является сущностью БД и представляет данные запроса на блокировку карты
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CardLockRequest extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Карта для блокировки
     */
    @OneToOne(fetch = FetchType.LAZY)
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardLockRequestStatus status;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CardLockRequest that = (CardLockRequest) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
