package com.sfood.eai.external.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional  // 데이터 롤백하려면 주석 해제
class GodoOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String jsonBody;

    @BeforeEach
    void setUp() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/fixtures/godo-order-invoice.json");
        jsonBody = new String(inputStream.readAllBytes());
    }

    @DisplayName("송장 정보 전송 테스트")
    @Test
    void sendInvoiceTest() throws Exception{
        mockMvc.perform(post("/godo/invoice-modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("company-code", "C00000001")
                        .header("user-id", "sfood-tester")
                        .content(jsonBody)
                ).andExpect(status().isOk());

    }
}