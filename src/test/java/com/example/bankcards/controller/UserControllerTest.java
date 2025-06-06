package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.dto.common.SortRequest;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.FilterUserRequest;
import com.example.bankcards.dto.user.RoleNameRequest;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.entity.RoleName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractTest {

    private static final String testUserName = "TestName";
    private static final String testPassword = "testPassword";
    private static final String roleAdmin = RoleName.ROLE_ADMIN.name();
    private static final String id = UUID.randomUUID().toString();

    @ParameterizedTest
    @MethodSource("createUser_invalidRequest_methodSource")
    void createUser_invalidRequest_expect_validationError(CreateUserRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("updateUser_invalidRequest_methodSource")
    void updateUser_invalidRequest_expect_validationError(UpdateUserRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(patch("/api/v1/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("findByFilter_invalidRequest_methodSource")
    void findByFilter_invalidRequest_expect_validationError(FilterUserRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(post("/api/v1/users/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    private static Stream<Arguments> createUser_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(new CreateUserRequest(null, testPassword, List.of(new RoleNameRequest(roleAdmin))),
                        "Имя пользователя не должно быть пустым или null"),
                Arguments.of(new CreateUserRequest("      ", testPassword, List.of(new RoleNameRequest(roleAdmin))),
                        "Имя пользователя не должно быть пустым или null"),
                Arguments.of(new CreateUserRequest("xyz", testPassword, List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина имени пользователя"),
                Arguments.of(new CreateUserRequest("****************", testPassword, List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина имени пользователя"),
                Arguments.of(new CreateUserRequest(testUserName, null, List.of(new RoleNameRequest(roleAdmin))),
                        "Пароль пользователя не должен быть пустым или null"),
                Arguments.of(new CreateUserRequest(testUserName, "       ", List.of(new RoleNameRequest(roleAdmin))),
                        "Пароль пользователя не должен быть пустым или null"),
                Arguments.of(new CreateUserRequest(testUserName, "xyz", List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина пароля пользователя"),
                Arguments.of(new CreateUserRequest(testUserName, "****************", List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина пароля пользователя"),
                Arguments.of(new CreateUserRequest(testUserName, testPassword, null),
                        "Список ролей пользователя не должен быть пустым или null"),
                Arguments.of(new CreateUserRequest(testUserName, testPassword, List.of()),
                        "Список ролей пользователя не должен быть пустым или null"),
                Arguments.of(new CreateUserRequest(testUserName, testPassword, List.of(new RoleNameRequest(null))),
                        "Имя роли не должно быть null"),
                Arguments.of(new CreateUserRequest(testUserName, testPassword, List.of(new RoleNameRequest("xyz"))),
                        "Недопустимое имя роли пользователя"));
    }

    private static Stream<Arguments> updateUser_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(new UpdateUserRequest("       ", List.of(new RoleNameRequest(roleAdmin))),
                        "Пароль пользователя не может быть пустым"),
                Arguments.of(new UpdateUserRequest("xyz", List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина пароля пользователя"),
                Arguments.of(new UpdateUserRequest("****************", List.of(new RoleNameRequest(roleAdmin))),
                        "Недопустимая длина пароля пользователя"),
                Arguments.of(new UpdateUserRequest(testPassword, List.of(new RoleNameRequest(null))),
                        "Имя роли не должно быть null"),
                Arguments.of(new UpdateUserRequest(testPassword, List.of(new RoleNameRequest("xyz"))),
                        "Недопустимое имя роли пользователя"));
    }

    private static Stream<Arguments> findByFilter_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(new FilterUserRequest(null, null, List.of(new RoleNameRequest(null)), null),
                        "Имя роли не должно быть null"),
                Arguments.of(new FilterUserRequest(null, null, List.of(new RoleNameRequest("xyz")), null),
                        "Недопустимое имя роли пользователя"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(null, 1, null))
                                .build(),
                        "Не задан номер страницы"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(0, 1, null))
                                .build(),
                        "Номер страницы не может быть меньше 1"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(1, null, null))
                                .build(),
                        "Не задан размер страницы"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(1, 0, null))
                                .build(),
                        "Размер страницы не может быть меньше 1"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest(null, null))))
                                .build(),
                        "Не задано поле для сортировки"),
                Arguments.of(FilterUserRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest("field", "xyz"))))
                                .build(),
                        "Указано некорректное направление сортировки")
        );
    }
}