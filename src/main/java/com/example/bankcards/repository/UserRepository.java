package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
        FROM User u
        JOIN FETCH u.roles
        WHERE :id IS NOT NULL AND u.id = :id
        """)
    Optional<User> findByIdWithRoles(UUID id);

    Optional<User> findByUserName(String userName);

    @Query("""
        DELETE FROM User
        WHERE :userId IS NOT NULL AND id = :userId
        """)
    @Modifying
    void deleteUserById(@Param("userId")UUID userId);

    @Query(value = """
            SELECT
                u.id,
                u.user_name,
                u.encoded_password,
                u.create_time,
                u.create_user,
                u.last_update_time,
                u.last_update_user
            FROM users u
            JOIN users_role ur ON u.id = ur.user_id
            JOIN role r ON ur.role_id = r.id
            WHERE ((:roles) IS NULL OR r.role_name IN (:roles))
                AND (cast(:createTimeFrom AS DATE) IS NULL OR u.create_time >= :createTimeFrom)
                AND (cast(:createTimeTo AS DATE) IS NULL OR u.create_time <= :createTimeTo)
            GROUP BY u.id
        """, nativeQuery = true)
    List<User> findByFilter(@Param("roles") List<String> roles,
                            @Param("createTimeFrom") LocalDateTime createTimeFrom,
                            @Param("createTimeTo") LocalDateTime createTimeTo,
                            Pageable pageable);

    @Query("""
        FROM User u
        JOIN FETCH u.roles
        WHERE u.id = :id
        """)
    Optional<User> findById(@Param("id") UUID id);
}
