package com.problemhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problemhub.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/admin/stats with Admin token should return metrics")
    void testGetAdminStatsSuccess() throws Exception {
        LoginRequest adminLogin = new LoginRequest("admin@problemhub.com", "admin123");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String adminToken = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProblems", greaterThanOrEqualTo(20)))
                .andExpect(jsonPath("$.totalUsers", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.totalTechnologies", greaterThanOrEqualTo(20)))
                .andExpect(jsonPath("$.totalTags", greaterThanOrEqualTo(20)));
    }

    @Test
    @DisplayName("GET /api/admin/stats with Student token should be forbidden (403)")
    void testGetAdminStatsForbiddenForStudent() throws Exception {
        LoginRequest studentLogin = new LoginRequest("student@problemhub.com", "student123");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String studentToken = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }
}
