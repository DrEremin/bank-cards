package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.card.*;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.dto.common.SortRequest;
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

class CardControllerTest extends AbstractTest {

    public static final String id = UUID.randomUUID().toString();
    private static final String roleAdmin = RoleName.ROLE_ADMIN.name();

    @ParameterizedTest
    @MethodSource("createCard_invalidRequest_methodSource")
    void createCard_invalidRequest_expect_validationError(CreateCardRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("updateCard_invalidRequest_methodSource")
    void updateCard_invalidRequest_expect_validationError(UpdateCardRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(patch("/api/v1/cards/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("findByFilter_invalidRequest_methodSource")
    void findByFilter_invalidRequest_expect_validationError(FilterCardRequest request, String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);
        mockMvc.perform(post("/api/v1/cards/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    private static Stream<Arguments> createCard_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(new CreateCardRequest(null, new ValidThruRequest(2025, 12)),
                        "Идентификатор владельца карты не должен быть null"),
                Arguments.of(new CreateCardRequest("xyz", new ValidThruRequest(2025, 12)),
                        "Идентификатор владельца карты не соотетствует формату UUID"),
                Arguments.of(new CreateCardRequest(id, null),
                        "Срок действия карты не должен быть null"),
                Arguments.of(new CreateCardRequest(id, new ValidThruRequest(null, 12)),
                        "Год срока действия карты не должен быть null"),
                Arguments.of(new CreateCardRequest(id, new ValidThruRequest(2024, 12)),
                        "Год срока действия карты не должен быть меньше 2025"),
                Arguments.of(new CreateCardRequest(id, new ValidThruRequest(2025, null)),
                        "Месяц срока действия карты не должен быть null"),
                Arguments.of(new CreateCardRequest(id, new ValidThruRequest(2025, 0)),
                        "Месяц срока действия карты не должен быть меньше 1"),
                Arguments.of(new CreateCardRequest(id, new ValidThruRequest(2025, 13)),
                        "Месяц срока действия карты не должен больше 12"));
    }

    private static Stream<Arguments> updateCard_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(
                        new UpdateCardRequest("xyz", new ValidThruRequest(2025, 12)),
                        "Недопустимое имя статуса активности карты"),
                Arguments.of(
                        new UpdateCardRequest(roleAdmin, new ValidThruRequest(null, 12)),
                        "Год срока действия карты не должен быть null"),
                Arguments.of(
                        new UpdateCardRequest(roleAdmin, new ValidThruRequest(2024, 12)),
                        "Год срока действия карты не должен быть меньше 2025"),
                Arguments.of(
                        new UpdateCardRequest(roleAdmin, new ValidThruRequest(2025, null)),
                        "Месяц срока действия карты не должен быть null"),
                Arguments.of(
                        new UpdateCardRequest(roleAdmin, new ValidThruRequest(2025, 0)),
                        "Месяц срока действия карты не должен быть меньше 1"),
                Arguments.of(
                        new UpdateCardRequest(roleAdmin, new ValidThruRequest(2025, 13)),
                        "Месяц срока действия карты не должен больше 12"));
    }

    private static Stream<Arguments> findByFilter_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(FilterCardRequest.builder()
                                .statuses(List.of(new CardStatusRequest(null)))
                                .build(),
                        "Имя статуса активности карты не должно быть null"),
                Arguments.of(FilterCardRequest.builder()
                                .statuses(List.of(new CardStatusRequest("xyz")))
                                .build(),
                        "Недопустимое имя статуса активности карты"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(null, 1, null))
                                .build(),
                        "Не задан номер страницы"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(0, 1, null))
                                .build(),
                        "Номер страницы не может быть меньше 1"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(1, null, null))
                                .build(),
                        "Не задан размер страницы"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(1, 0, null))
                                .build(),
                        "Размер страницы не может быть меньше 1"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest(null, null))))
                                .build(),
                        "Не задано поле для сортировки"),
                Arguments.of(FilterCardRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest("field", "xyz"))))
                                .build(),
                        "Указано некорректное направление сортировки"));
    }
}