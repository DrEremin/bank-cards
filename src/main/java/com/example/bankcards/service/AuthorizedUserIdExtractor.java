package com.example.bankcards.service;

import com.example.bankcards.security.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public interface AuthorizedUserIdExtractor {

    default UUID getAuthorizedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        return userInfo.getId();
    }
}
