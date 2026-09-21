package com.problemhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problemhub.dto.LoginRequest;
import com.problemhub.dto.RegisterRequest;
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
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/login with valid admin credentials should return 200 and JWT")
    void testLoginAdminSuccess() throws Exception {
        LoginRequest login = new LoginRequest("admin@problemhub.com", "admin123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.user.email").value("admin@problemhub.com"))
                .andExpect(jsonPath("$.user.role").value("ADMIN"));
    }

    @Test
    @DisplayName("POST /api/auth/login with valid student credentials should return 200 and JWT")
    void testLoginStudentSuccess() throws Exception {
        LoginRequest login = new LoginRequest("student@problemhub.com", "student123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.user.email").value("student@problemhub.com"))
                .andExpect(jsonPath("$.user.role").value("STUDENT"));
    }

    @Test
    @DisplayName("POST /api/auth/login with bad password should return 400")
    void testLoginBadPassword() throws Exception {
        LoginRequest login = new LoginRequest("admin@problemhub.com", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("invalid email or password")));
    }

    @Test
    @DisplayName("POST /api/auth/register should create new user and return JWT")
    void testRegisterNewUser() throws Exception {
        String uniqueEmail = "dev_" + System.currentTimeMillis() + "@problemhub.com";
        RegisterRequest register = new RegisterRequest("Maya Lin", uniqueEmail, "secretPass123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.user.name").value("Maya Lin"))
                .andExpect(jsonPath("$.user.email").value(uniqueEmail))
                .andExpect(jsonPath("$.user.role").value("STUDENT"));
    }

    @Test
    @DisplayName("GET /api/auth/me with Bearer token should return user profile")
    void testGetProfileWithToken() throws Exception {
        // First login to get token
        LoginRequest login = new LoginRequest("student@problemhub.com", "student123");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(response).get("token").asText();

        // Query /api/auth/me with Bearer header
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@problemhub.com"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }
}
