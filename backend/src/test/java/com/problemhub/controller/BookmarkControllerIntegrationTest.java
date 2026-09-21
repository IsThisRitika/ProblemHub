package com.problemhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problemhub.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookmarkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String studentToken;

    @BeforeEach
    void setUp() throws Exception {
        LoginRequest login = new LoginRequest("student@problemhub.com", "student123");
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        studentToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @DisplayName("GET /api/users/me/bookmarks should return student's bookmarked problems")
    void testGetStudentBookmarks() throws Exception {
        mockMvc.perform(get("/api/users/me/bookmarks")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].id", hasItem(1)));
    }

    @Test
    @DisplayName("POST and DELETE /api/problems/{id}/bookmark lifecycle")
    void testAddAndRemoveBookmark() throws Exception {
        // 1. Add bookmark for problem 5
        mockMvc.perform(post("/api/problems/5/bookmark")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmarked").value(true));

        // 2. Verify problem 5 is in bookmarks
        mockMvc.perform(get("/api/users/me/bookmarks")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(5)));

        // 3. Remove bookmark for problem 5
        mockMvc.perform(delete("/api/problems/5/bookmark")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookmarked").value(false));
    }

    @Test
    @DisplayName("GET /api/users/me/bookmarks without token should be unauthorized")
    void testGetBookmarksUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me/bookmarks"))
                .andExpect(status().isForbidden());
    }
}
