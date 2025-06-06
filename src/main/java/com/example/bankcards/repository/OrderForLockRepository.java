package com.example.bankcards.repository;

import com.example.bankcards.entity.OrderForLock;
import com.example.bankcards.entity.OrderForLockStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderForLockRepository extends JpaRepository<OrderForLock, UUID> {

    @Query("""
        FROM OrderForLock order
        JOIN FETCH order.card
        WHERE (:cardId IS NULL OR order.card.id = :cardId)
            AND (:statuses IS NULL OR order.status IN (:statuses))
            AND (cast(:createTimeFrom AS DATE) IS NULL OR order.createTime >= :createTimeFrom)
            AND (cast(:createTimeTo AS DATE) IS NULL OR order.createTime <= :createTimeTo)
        """)
    List<OrderForLock> findByFilter(@Param("cardId") UUID cardId,
                                    @Param("statuses") List<OrderForLockStatus> statuses,
                                    @Param("createTimeFrom") LocalDateTime createTimeFrom,
                                    @Param("createTimeTo") LocalDateTime createTimeTo,
                                    Pageable pageable);

    @Query(value = """
        DELETE FROM order_for_lock
        WHERE :cardId IS NOT NULL 
            AND card_id = :cardId
        """, nativeQuery = true)
    @Modifying
    void deleteByCardId(@Param("cardId") UUID cardId);

    Optional<OrderForLock> findByCardId(UUID cardId);
}
