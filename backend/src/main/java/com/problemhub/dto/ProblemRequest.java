package com.problemhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ProblemRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Difficulty is required")
    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED

    @NotBlank(message = "Project type is required")
    private String projectType; // HACKATHON, MINI_PROJECT, MAJOR_PROJECT, FINAL_YEAR_PROJECT

    private String impact;
    private String solutionDirection;
    private String expectedOutcome;
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, ARCHIVED

    private List<Long> technologyIds = new ArrayList<>();
    private List<Long> tagIds = new ArrayList<>();

    public ProblemRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getSolutionDirection() {
        return solutionDirection;
    }

    public void setSolutionDirection(String solutionDirection) {
        this.solutionDirection = solutionDirection;
    }

    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    public void setExpectedOutcome(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getTechnologyIds() {
        return technologyIds;
    }

    public void setTechnologyIds(List<Long> technologyIds) {
        this.technologyIds = technologyIds != null ? technologyIds : new ArrayList<>();
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds != null ? tagIds : new ArrayList<>();
    }
}
