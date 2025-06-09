package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    @Query("""
        FROM Card card
        JOIN FETCH card.owner
        WHERE (:ownerId IS NULL OR card.owner.id = :ownerId)
            AND (cast(:createTimeFrom AS DATE) IS NULL OR card.createTime >= :createTimeFrom)
            AND (cast(:createTimeTo AS DATE) IS NULL OR card.createTime <= :createTimeTo)
            AND (cast(:expiredTimeForm AS DATE) IS NULL OR card.expiredTime >= :expiredTimeForm)
            AND (cast(:expiredTimeTo AS DATE) IS NULL OR card.expiredTime <= :expiredTimeTo)
            AND (:statuses IS NULL OR card.status IN (:statuses))
        """)
    List<Card> findByFilter(@Param("ownerId") UUID ownerId,
                            @Param("createTimeFrom") LocalDateTime createTimeFrom,
                            @Param("createTimeTo") LocalDateTime createTimeTo,
                            @Param("expiredTimeForm") LocalDateTime expiredTimeForm,
                            @Param("expiredTimeTo") LocalDateTime expiredTimeTo,
                            @Param("statuses") List<CardStatus> statuses,
                            Pageable pageable);

    @Query("""
        DELETE FROM Card
        WHERE :id IS NOT NULL
            AND id = :id
        """)
    @Modifying
    void deleteById(@Param("id") UUID id);

    @Query("""
        FROM Card card
        JOIN FETCH card.owner
        WHERE (:ownerId IS NOT NULL AND card.owner.id = :ownerId)
        """)
    List<Card> findAllByUserId(@Param("ownerId") UUID ownerId, Pageable pageable);

    @Query("""
        FROM Card c
        JOIN FETCH c.owner
        WHERE :id IS NOT NULL AND c.id = :id
        """)
    Optional<Card> findByIdWithUser(@Param("id") UUID id);

    @Query("""
        UPDATE Card card
        SET card.status = :newStatus
        WHERE (cast(:expiredTime AS DATE) IS NOT NULL AND card.expiredTime <= :expiredTime)
            AND (:newStatus IS NOT NULL AND card.status != :newStatus)
        """)
    @Modifying
    Integer updateCardsStatusLessThanExpiredTime(@Param("expiredTime") LocalDateTime expiredTime,
                                                 @Param("newStatus") CardStatus newStatus);
}