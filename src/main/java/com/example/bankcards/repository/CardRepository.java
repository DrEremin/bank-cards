package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    @Query("""
        FROM Card card
        JOIN FETCH card.owner
        WHERE (:ownerId IS NULL OR card.owner.id = :ownerId)
            AND (cast(:expiredTimeForm AS DATE) IS NULL OR card.expiredTime >= :expiredTimeForm)
            AND (cast(:expiredTimeTo AS DATE) IS NULL OR card.expiredTime <= :expiredTimeTo)
            AND (:statuses IS NULL OR card.status IN (:statuses))
            AND (:deleted IS NULL OR card.deleted = :deleted)
        """)
    List<Card> findByFilter(@Param("ownerId") UUID ownerId,
                            @Param("expiredTimeForm") LocalDateTime expiredTimeForm,
                            @Param("expiredTimeTo") LocalDateTime expiredTimeTo,
                            @Param("statuses") List<CardStatus> statuses,
                            @Param("deleted") Boolean deleted);
}
