package com.example.bankcards.repository;

import com.example.bankcards.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

    @Query(value = """
        DELETE FROM users_role
        WHERE :userId IS NOT NULL
            AND user_id = :userId
        """, nativeQuery = true)
    @Modifying
    void deleteByUserId(@Param("userId") UUID userId);
}
