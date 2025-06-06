package com.example.bankcards.service.impl;

import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.dto.user.*;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BankCardsException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.UniquenessViolationException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.UserRoleRepository;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.mapper.PageableMapper;
import com.example.bankcards.util.mapper.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CardRepository cardRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        String userName = request.getUserName();

        if (userRepository.findByUserName(userName).isPresent()) {
            throw new UniquenessViolationException("Пользователь с именем %s не найден".formatted(userName));
        }

        List<String> names = getNamesFromRoleNameRequests(request.getRoles());
        User user = userRepository.save(buildUser(userName, request.getPassword(), names));

        return UserResponseMapper.fromUserWithRoles(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    cardRepository.deleteById(user.getId());
                    userRoleRepository.deleteByUserId(user.getId());
                    userRepository.deleteUserById(userId);
                });
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID: %s не найден".formatted(userId)));

        if (request.getNewPassword() != null) {
            user.setEncodedPassword(request.getNewPassword());
        }

        if (request.getNewRoles() != null) {
            if (request.getNewRoles().isEmpty()) {
                throw new BankCardsException("Нет списка для обновления ролей пользователя с ID: %s".formatted(userId));
            }

            List<String> roleNames = getNamesFromRoleNameRequests(request.getNewRoles());
            Set<Role> newRoles = findRolesByNames(roleNames);
            user.setRoles(newRoles);
        }

        return UserResponseMapper.fromUserWithRoles(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findByFilter(FilterUserRequest request) {
        PageableRequest page = request.getPage();
        Pageable pageable = PageableMapper.mapPageable(page);
        List<String> roleNames = null;

        if (request.getRoles() != null) {
            roleNames = getNamesFromRoleNameRequests(request.getRoles());
        }

        return userRepository.findByFilter(roleNames, request.getCreateTimeFrom(), request.getCreateTimeTo(), pageable)
                .stream()
                .map(UserResponseMapper::fromUser)
                .toList();
    }

    @Override
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID: %s не найден".formatted(id)));
        return UserResponseMapper.fromUserWithRoles(user);
    }

    private List<String> getNamesFromRoleNameRequests(List<RoleNameRequest> roleNameRequests) {
        return roleNameRequests.stream()
                .map(RoleNameRequest::getRole)
                .toList();
    }

    private User buildUser(String userName, String password, List<String> names) {
        Set<Role> roles = findRolesByNames(names);

        return User.builder()
                .userName(userName)
                .encodedPassword(passwordEncoder.encode(password))
                .roles(roles)
                .build();
    }

    private Set<Role> findRolesByNames(List<String> names) {
        List<RoleName> roleNames = names.stream()
                .map(RoleName::valueOf)
                .toList();

        return roleRepository.findByRoleNames(roleNames);
    }
}