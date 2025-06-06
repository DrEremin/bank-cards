package com.example.bankcards.entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {

    public static final String DEFAULT_DB_USER = "bank-cards";

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
        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = DEFAULT_DB_USER;
        this.lastUpdateUser = DEFAULT_DB_USER;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateTime = LocalDateTime.now();
        this.lastUpdateUser = DEFAULT_DB_USER;
    }
}
