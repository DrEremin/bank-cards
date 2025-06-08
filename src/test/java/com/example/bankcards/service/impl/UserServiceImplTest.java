package com.example.bankcards.service.impl;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.RoleNameRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.UniquenessViolationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest extends AbstractTest {

    private final UUID id = UUID.randomUUID();
    private final String password = "123";
    private final String userName = "Test";

    private final String roleUser = RoleName.ROLE_USER.name();

    private final CreateUserRequest createUserRequest = CreateUserRequest.builder()
            .userName(userName)
            .password(password)
            .roles(List.of(new RoleNameRequest(roleUser)))
            .build();

    private final UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
            .newPassword(password)
            .newRoles(List.of(new RoleNameRequest(roleUser)))
            .build();

    private final User user = User.builder()
            .id(id)
            .encodedPassword(password)
            .build();

    @Test
    void createUser_userAlreadyExists_expect_uniquenessViolationException() {
        var userName = createUserRequest.getUserName();
        when(mockUserRepository.findByUserName(eq(userName)))
                .thenReturn(Optional.of(new User()));

        Exception ex = assertThrowsExactly(UniquenessViolationException.class,
                () -> userService.createUser(createUserRequest));
        assertEquals("Операция отклонена, пользователь с именем Test уже существует", ex.getMessage());
    }

    @Test
    void createUser_userDoesNotExist_expect_Success() {
        var userName = createUserRequest.getUserName();
        Set<Role> roles = Set.of(new Role(UUID.randomUUID(), RoleName.ROLE_USER));
        when(mockRoleRepository.findByRoleNames(eq(List.of(RoleName.ROLE_USER))))
                .thenReturn(roles);
        when(mockUserRepository.save(any(User.class)))
                .thenReturn(new User(UUID.randomUUID(), userName, createUserRequest.getPassword(), roles, null));
        when(mockPasswordEncoder.encode(anyString()))
                .thenReturn("test password");

        userService.createUser(createUserRequest);

        verify(mockRoleRepository)
                .findByRoleNames(eq(List.of(RoleName.ROLE_USER)));
        verify(mockPasswordEncoder)
                .encode(anyString());
        verify(mockUserRepository)
                .save(any(User.class));
    }

    @Test
    void deleteUser_userDoesNotExist_expect_Success() {
        UUID userId = UUID.randomUUID();
        when(mockUserRepository.findById(eq(userId)))
                .thenReturn(Optional.empty());

        userService.deleteUser(userId);

        verify(mockUserRepository)
                .findById(eq(userId));
        verify(mockCardRepository, never())
                .deleteById(eq(userId));
        verify(mockUserRoleRepository, never())
                .deleteByUserId(eq(userId));
    }

    @Test
    void deleteUser_userIsExists_expect_Success() {
        UUID userId = user.getId();
        when(mockUserRepository.findById(eq(userId)))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(mockUserRepository)
                .findById(eq(userId));
        verify(mockUserRepository)
                .deleteUserById(userId);
        verify(mockCardRepository)
                .deleteById(eq(userId));
        verify(mockUserRoleRepository)
                .deleteByUserId(eq(userId));
    }

    @Test
    void updateUser_userDoesNotExist_expect_entityNotFoundException() {
        UUID userId = user.getId();
        when(mockUserRepository.findByIdWithRoles(userId))
                .thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(EntityNotFoundException.class,
                () -> userService.updateUser(userId, updateUserRequest));
        assertEquals("Операция отклонена, пользователь не найден", ex.getMessage());

        verify(mockUserRepository)
                .findByIdWithRoles(eq(userId));
        verify(mockRoleRepository, never())
                .findByRoleNames(any(List.class));
    }

    @Test
    void updateUser_emptyListOfNewRoles_expect_bankCardsException() {
        UUID userId = user.getId();
        when(mockUserRepository.findByIdWithRoles(userId))
                .thenReturn(Optional.of(user));
        updateUserRequest.setNewRoles(List.of());

        Exception ex = assertThrowsExactly(BankCardsException.class,
                () -> userService.updateUser(userId, updateUserRequest));
        assertEquals("Операция отклонена. Нет списка для обновления ролей пользователя", ex.getMessage());

        verify(mockUserRepository)
                .findByIdWithRoles(eq(userId));
        verify(mockRoleRepository, never())
                .findByRoleNames(any(List.class));
    }

    @Test
    void updateUser_validUpdateUserRequest_expect_success() {
        UUID userId = user.getId();
        when(mockUserRepository.findByIdWithRoles(userId))
                .thenReturn(Optional.of(user));
        when(mockRoleRepository.findByRoleNames(any(List.class)))
                .thenReturn(Set.of(new Role(id, RoleName.ROLE_ADMIN)));
        when(mockUserRepository.save(eq(user)))
                .thenReturn(user);

        userService.updateUser(userId, updateUserRequest);

        verify(mockUserRepository)
                .findByIdWithRoles(eq(userId));
        verify(mockUserRepository)
                .save(eq(user));
        verify(mockRoleRepository)
                .findByRoleNames(any(List.class));
    }
}