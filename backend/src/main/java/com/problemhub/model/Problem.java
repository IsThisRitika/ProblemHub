package com.problemhub.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Problem {
    private Long id;
    private String title;
    private String description;
    private String domain;
    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED
    private String projectType; // HACKATHON, MINI_PROJECT, MAJOR_PROJECT, FINAL_YEAR_PROJECT
    private String impact;
    private String solutionDirection;
    private String expectedOutcome;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Technology> technologies = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public Problem() {
    }

    public Problem(Long id, String title, String description, String domain, String difficulty,
                   String projectType, String impact, String solutionDirection, String expectedOutcome,
                   String status, Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.difficulty = difficulty;
        this.projectType = projectType;
        this.impact = impact;
        this.solutionDirection = solutionDirection;
        this.expectedOutcome = expectedOutcome;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies != null ? technologies : new ArrayList<>();
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }
}
