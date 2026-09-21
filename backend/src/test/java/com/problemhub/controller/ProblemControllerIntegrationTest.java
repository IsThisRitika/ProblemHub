package com.problemhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.problemhub.dto.ProblemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProblemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/problems should return paginated list of problem statements")
    void testGetAllProblems() throws Exception {
        mockMvc.perform(get("/api/problems?page=0&size=5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(5))))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(5))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(20)));
    }

    @Test
    @DisplayName("GET /api/problems/1 should return problem details with technologies and tags")
    void testGetProblemById() throws Exception {
        mockMvc.perform(get("/api/problems/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.domain").isNotEmpty())
                .andExpect(jsonPath("$.technologies", not(empty())))
                .andExpect(jsonPath("$.tags", not(empty())));
    }

    @Test
    @DisplayName("GET /api/problems/99999 should return 404 Not Found")
    void testGetProblemNotFound() throws Exception {
        mockMvc.perform(get("/api/problems/99999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin@problemhub.com", roles = {"ADMIN"})
    @DisplayName("POST, PUT, and DELETE /api/problems CRUD lifecycle")
    void testProblemCrudLifecycle() throws Exception {
        // 1. Create a new problem
        ProblemRequest createRequest = new ProblemRequest();
        createRequest.setTitle("Real-Time Wildfire Detection Drone Network");
        createRequest.setDescription("Automated mesh of edge-computing thermal camera drones for early forest fire triangulation.");
        createRequest.setDomain("Civic Tech");
        createRequest.setDifficulty("ADVANCED");
        createRequest.setProjectType("FINAL_YEAR_PROJECT");
        createRequest.setImpact("Drastically reduces wildfire spread through sub-minute ignition alerts.");
        createRequest.setSolutionDirection("Deploy edge YOLO models on low-power drone hardware with LoRaWAN mesh relay.");
        createRequest.setExpectedOutcome("Live heat map and telemetry dashboard.");
        createRequest.setStatus("PUBLISHED");
        createRequest.setTechnologyIds(List.of(1L, 2L)); // Java, Spring Boot
        createRequest.setTagIds(List.of(1L, 10L)); // Machine Learning, IoT

        String responseBody = mockMvc.perform(post("/api/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("Real-Time Wildfire Detection Drone Network"))
                .andExpect(jsonPath("$.technologies", hasSize(2)))
                .andReturn().getResponse().getContentAsString();

        long createdId = objectMapper.readTree(responseBody).get("id").asLong();

        // 2. Update the problem
        createRequest.setTitle("Real-Time Wildfire Detection Drone Network (Updated)");
        createRequest.setDifficulty("INTERMEDIATE");

        mockMvc.perform(put("/api/problems/" + createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Real-Time Wildfire Detection Drone Network (Updated)"))
                .andExpect(jsonPath("$.difficulty").value("INTERMEDIATE"));

        // 3. Delete the problem
        mockMvc.perform(delete("/api/problems/" + createdId))
                .andExpect(status().isNoContent());

        // 4. Verify it's gone
        mockMvc.perform(get("/api/problems/" + createdId))
                .andExpect(status().isNotFound());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = {"ADMIN"})
    @DisplayName("POST /api/problems with invalid payload should return 400 Bad Request")
    void testCreateProblemValidationFailure() throws Exception {
        ProblemRequest invalidRequest = new ProblemRequest();
        invalidRequest.setTitle(""); // Blank title

        mockMvc.perform(post("/api/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").exists());
    }
}
