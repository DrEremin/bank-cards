package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.common.CommonRequest;
import com.example.bankcards.dto.transfer.TransferRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransferControllerTest extends AbstractTest {

    private static final String id = UUID.randomUUID().toString();

    @ParameterizedTest
    @MethodSource("createTransfer_invalidRequest_methodSource")
    void createTransfer_invalidRequest_expect_validationError(TransferRequest request,
                                                              String errMessage) throws Exception {
        var commonRequest = new CommonRequest<>(request);
        var requestBody = objectMapper.writeValueAsString(commonRequest);

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage", equalTo("Ошибка валидации")))
                .andExpect(jsonPath("$.validationErrors.[?(@.message == \"" + errMessage + "\")]").exists());
    }

    private static Stream<Arguments> createTransfer_invalidRequest_methodSource() {
        return Stream.of(
                Arguments.of(new TransferRequest(null, id, "1.00"),
                        "Идентификатор карты-источника не должен быть null"),
                Arguments.of(new TransferRequest("xyz", id, "1.00"),
                        "Идентификатор карты-источника не соотетствует формату UUID"),
                Arguments.of(new TransferRequest(id, null, "1.00"),
                        "Идентификатор карты назначения не должен быть null"),
                Arguments.of(new TransferRequest(id, "xyz", "1.00"),
                        "Идентификатор карты назначения не соотетствует формату UUID"),
                Arguments.of(new TransferRequest(id, id, null),
                        "Сумма перевода не должна быть null"),
                Arguments.of(new TransferRequest(id, id, "1"),
                        "Некорректный формат суммы перевода"));
    }
}