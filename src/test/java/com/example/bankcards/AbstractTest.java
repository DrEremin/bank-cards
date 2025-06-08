package com.example.bankcards;

import com.example.bankcards.entity.*;
import com.example.bankcards.repository.*;
import com.example.bankcards.security.UserInfo;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.OrderForLockService;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.property.ValidPeriodProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractTest {

    public static final User user = User.builder()
            .id(UUID.randomUUID())
            .userName("Test")
            .roles(Set.of(new Role(UUID.randomUUID(), RoleName.ROLE_ADMIN),
                    new Role(UUID.randomUUID(), RoleName.ROLE_USER))
            )
            .build();

    public final Card card = Card.builder()
            .id(UUID.randomUUID())
            .encodedNumber("1234567891234567")
            .owner(user)
            .status(CardStatus.ACTIVE)
            .balance(new BigDecimal("0.00"))
            .expiredTime(LocalDateTime.now().plusYears(2))
            .build();

    public static final UserInfo userDetails = new UserInfo(user);

    public static final Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
    );

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected CardService cardService;

    @Autowired
    protected OrderForLockService orderForLockService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected TransferService transferService;

    @MockitoBean
    protected ValidPeriodProperty mockValidPeriodProperty;

    @MockitoBean
    protected CardRepository mockCardRepository;

    @MockitoBean
    protected UserRepository mockUserRepository;

    @MockitoBean
    protected RoleRepository mockRoleRepository;

    @MockitoBean
    protected UserRoleRepository mockUserRoleRepository;

    @MockitoBean
    protected OrderForLockRepository mockOrderForLockRepository;

    @MockitoBean
    protected BasicTextEncryptor mockBasicTextEncryptor;

    @MockitoBean
    protected PasswordEncoder mockPasswordEncoder;

    @MockitoBean
    protected TransferRepository mockTransferRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
