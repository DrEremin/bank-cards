package com.example.bankcards.repository;

import com.example.bankcards.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
}
