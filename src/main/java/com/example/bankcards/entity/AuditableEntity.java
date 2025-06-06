package com.example.bankcards.entity;

import com.example.bankcards.security.UserInfo;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {

    /**
     * Время создания записи
     */
    private LocalDateTime createTime;
    /**
     * Время последнего обновления записи
     */
    private LocalDateTime lastUpdateTime;
    /**
     * Имя пользователя, создавшего запись
     */
    private String createUser;
    /**
     * Имя пользователя, сделавшего последние изменения
     */
    private String lastUpdateUser;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        String userName = getUserName();
        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = userName;
        this.lastUpdateUser = userName;
    }

    @PreUpdate
    public void preUpdate() {
        String userName = getUserName();
        this.lastUpdateTime = LocalDateTime.now();
        this.lastUpdateUser = userName;
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        return userInfo.getUsername();
    }
}
