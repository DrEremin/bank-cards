package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Этот класс является сущностью БД для обеспечения связи M:N пользователя и роли
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_role")
public class UserRole extends BaseEntity {

    @EmbeddedId
    private UserRoleId id;

    @Getter
    @Setter
    @Embeddable
    public static class UserRoleId implements Serializable {

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "role_id")
        private UUID roleId;

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            UserRoleId that = (UserRoleId) object;
            return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }
    }
}
