package com.example.bankcards.repository;

import com.example.bankcards.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, AppUserRole.AppUserRoleId> {
}
