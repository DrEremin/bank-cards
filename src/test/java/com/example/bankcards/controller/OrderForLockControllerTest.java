package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.orderforlock.OrderForLockStatusRequest;
import com.example.bankcards.dto.orderforlock.CreateOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.FilterOrderForLockRequest;
import com.example.bankcards.dto.orderforlock.UpdateOrderForLockRequest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.common.PageableRequest;
import com.example.bankcards.dto.common.SortRequest;
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

class OrderForLockControllerTest extends AbstractTest {

    public static final String id = UUID.randomUUID().toString();

    @ParameterizedTest
    @MethodSource("createOrderForLock_invalidRequest_MethodSource")
    void createOrderForLock_ForLock_invalidRequest_expect_validationError(CreateOrderForLockRequest request,
                                                                   String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);

        mockMvc.perform(post("/api/v1/orders-for-locks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("updateOrderForLock_invalidRequest_MethodSource")
    void updateOrderForLock_ForLock_invalidRequest_expect_validationError(UpdateOrderForLockRequest request,
                                                                   String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);

        mockMvc.perform(patch("/api/v1/orders-for-locks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    @ParameterizedTest
    @MethodSource("findByFilter_invalidRequest_MethodSource")
    void findByFilter_invalidRequest_expect_validationError(FilterOrderForLockRequest request,
                                                            String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);

        mockMvc.perform(post("/api/v1/orders-for-locks/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    private static Stream<Arguments> createOrderForLock_invalidRequest_MethodSource() {
        return Stream.of(
                Arguments.of(new CreateOrderForLockRequest(null), "Идентификатор карты не должен быть null"),
                Arguments.of(new CreateOrderForLockRequest("xyz"), "Идентификатор карты не соотетствует формату UUID")
        );
    }

    private static Stream<Arguments> updateOrderForLock_invalidRequest_MethodSource() {
        return Stream.of(
                Arguments.of(new UpdateOrderForLockRequest(null),
                        "Действие по заявке на блокировку карты не должно быть null"),
                Arguments.of(new UpdateOrderForLockRequest("xyz"),
                        "Недопустимое имя действия по заявке на блокировку карты")
        );
    }

    private static Stream<Arguments> findByFilter_invalidRequest_MethodSource() {
        return Stream.of(
                Arguments.of(FilterOrderForLockRequest.builder()
                                .statuses(List.of(new OrderForLockStatusRequest(null)))
                                .build(),
                        "Имя статуса заявки на блокировку карты не должно быть null"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .statuses(List.of(new OrderForLockStatusRequest("xyz")))
                                .build(),
                        "Недопустимое имя статуса заявки на блокировку карты"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(null, 1, null))
                                .build(),
                        "Не задан номер страницы"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(0, 1, null))
                                .build(),
                        "Номер страницы не может быть меньше 1"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(1, null, null))
                                .build(),
                        "Не задан размер страницы"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(1, 0, null))
                                .build(),
                        "Размер страницы не может быть меньше 1"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest(null, null))))
                                .build(),
                        "Не задано поле для сортировки"),
                Arguments.of(FilterOrderForLockRequest.builder()
                                .page(new PageableRequest(1, 1, List.of(new SortRequest("field", "xyz"))))
                                .build(),
                        "Указано некорректное направление сортировки")
        );
    }
}