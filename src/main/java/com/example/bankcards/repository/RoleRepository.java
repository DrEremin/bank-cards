package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("""
        FROM Role r
        WHERE r.roleName IN (:roleNames)
        """)
    Set<Role> findByRoleNames(List<String> roleNames);
}
