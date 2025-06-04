package com.example.bankcards.entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Этот класс добавляет поля атрибутов для аудирования записей в БД
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

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
    /**
     * Признак удаления записи
     */
    private Boolean deleted;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = DEFAULT_DB_USER;
        this.lastUpdateUser = DEFAULT_DB_USER;
        this.deleted = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateTime = LocalDateTime.now();
        this.lastUpdateUser = DEFAULT_DB_USER;
    }
}
