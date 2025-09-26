package com.haiilo.kata.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SupermarketCheckoutControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static String body(List<String> items) throws Exception {
        return new ObjectMapper().writeValueAsString(new Request(items));
    }

    private record Request(List<String> items) {

    }

    @Test
    void checkout_returns200_andTotal_onHappyPath() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(List.of("APPLE", "BANANA", "APPLE"))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalPriceInMinor").value(95));
    }

    @Test
    void checkout_returns400_forUnknownItem() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(List.of("APPLE", "PINEAPPLE"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void checkout_returns400_whenItemsNull() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void checkout_returns400_whenItemsEmptyArray() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(List.of())))
            .andExpect(status().isBadRequest());
    }

    @Test
    void checkout_acceptsLowercaseItems_andReturnsCorrectTotal() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(List.of("apple", "banana", "apple"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPriceInMinor").value(95));
    }

    @Test
    void checkout_responseContract_containsOnlyTotalPriceInMinor() throws Exception {
        mockMvc.perform(post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body(List.of("APPLE"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPriceInMinor").exists())
            .andExpect(jsonPath("$.timestamp").doesNotExist())
            .andExpect(jsonPath("$.status").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist())
            .andExpect(jsonPath("$.message").doesNotExist());
    }
}


