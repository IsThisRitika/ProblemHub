package com.problemhub.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProblemSearchFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Search by keyword should match title or description")
    void testSearchByKeyword() throws Exception {
        mockMvc.perform(get("/api/problems/search?keyword=vaccine")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[0].title", containsStringIgnoringCase("insulin")));
    }

    @Test
    @DisplayName("Filter by domain should only return problems from that domain")
    void testFilterByDomain() throws Exception {
        mockMvc.perform(get("/api/problems?domain=Healthcare")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].domain", everyItem(equalToIgnoringCase("Healthcare"))));
    }

    @Test
    @DisplayName("Filter by difficulty should only return problems with matching difficulty")
    void testFilterByDifficulty() throws Exception {
        mockMvc.perform(get("/api/problems?difficulty=BEGINNER")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].difficulty", everyItem(equalTo("BEGINNER"))));
    }

    @Test
    @DisplayName("Filter by projectType should only return matching project types")
    void testFilterByProjectType() throws Exception {
        mockMvc.perform(get("/api/problems?projectType=HACKATHON")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].projectType", everyItem(equalTo("HACKATHON"))));
    }

    @Test
    @DisplayName("Filter by technology should return problems that use that technology")
    void testFilterByTechnology() throws Exception {
        mockMvc.perform(get("/api/problems")
                        .param("technology", "Spring Boot")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].technologies[*].name", hasItem(equalToIgnoringCase("Spring Boot"))));
    }

    @Test
    @DisplayName("Filter by tag should return problems containing that tag")
    void testFilterByTag() throws Exception {
        mockMvc.perform(get("/api/problems?tag=IoT")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].tags[*].name", hasItem(equalToIgnoringCase("IoT"))));
    }

    @Test
    @DisplayName("Combined filter: domain and difficulty")
    void testCombinedFilter() throws Exception {
        mockMvc.perform(get("/api/problems?domain=Healthcare&difficulty=INTERMEDIATE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[*].domain", everyItem(equalToIgnoringCase("Healthcare"))))
                .andExpect(jsonPath("$.content[*].difficulty", everyItem(equalTo("INTERMEDIATE"))));
    }

    @Test
    @DisplayName("Sorting by title ascending")
    void testSorting() throws Exception {
        mockMvc.perform(get("/api/problems?sortBy=title&sortDir=ASC&size=5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)));
    }
}
