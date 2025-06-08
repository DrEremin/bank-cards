package com.example.bankcards.security;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.property.SecurityProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminInitializerService {

    public static final String FIRST_ADMIN_USERNAME = "root";

    private final SecurityProperty securityProperty;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public void initialize() {
        userRepository.findByUserName(AdminInitializerService.FIRST_ADMIN_USERNAME)
                .ifPresentOrElse(this::updateUser, this::createUser);
        log.info("Инициализация/обновление первого администратора выполнена успешно");
    }

    private void updateUser(User user) {
        if (!passwordEncoder.matches(securityProperty.getFirstAdminPassword(), user.getEncodedPassword())) {
            user.setEncodedPassword(getEncodedPassword());
            authenticate(user);
            userRepository.save(user);
        }
    }

    private String getEncodedPassword() {
        return passwordEncoder.encode(securityProperty.getFirstAdminPassword());
    }

    private void createUser() {
        Set<Role> roles = roleRepository.findByRoleNames(List.of(RoleName.ROLE_ADMIN));
        User user = User.builder()
                .userName(FIRST_ADMIN_USERNAME)
                .encodedPassword(getEncodedPassword())
                .roles(roles)
                .build();
        authenticate(user);
        userRepository.save(user);
    }

    private void authenticate(User user) {
        UserDetails userDetails = new UserInfo(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
