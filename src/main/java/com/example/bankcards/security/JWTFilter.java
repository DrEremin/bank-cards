package com.example.bankcards.security;

import com.auth0.jwt.exceptions.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final JWTProvider jwtProvider;
    private final UserInfoService userInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        getTokenFromRequest(request)
                .flatMap(this::validateToken)
                .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotBlank(bearer) && bearer.startsWith(BEARER)) {
            return Optional.of(bearer.substring(7));
        }

        return Optional.empty();
    }

    private Optional<Authentication> validateToken(String token) {
        UsernamePasswordAuthenticationToken auth = null;

        try {
            String userName = jwtProvider.validateTokenAndRetrieveClaim(token);
            UserDetails userDetails = userInfoService.loadUserByUsername(userName);
            auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (TokenExpiredException e) {
            log.warn("Ошибка, время жизни идентификационного токена истекло");
        } catch (JWTDecodeException e) {
            log.warn("Ошибка декодирования идентификационного токена");
        } catch (SignatureVerificationException e) {
            log.warn("Ошибка проверки подписи идентификационного токена");
        } catch (AlgorithmMismatchException e) {
            log.warn("Ошибка, идентификационный токен закодирован другим алгоритмом");
        } catch (InvalidClaimException e) {
            log.warn("Ошибка, проверка данных подлинности пользователя не пройдена");
        }

        return Optional.ofNullable(auth);
    }
}