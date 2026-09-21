package com.problemhub.model;

import java.time.LocalDateTime;

public class Bookmark {
    private Long userId;
    private Long problemId;
    private LocalDateTime createdAt;

    public Bookmark() {
    }

    public Bookmark(Long userId, Long problemId, LocalDateTime createdAt) {
        this.userId = userId;
        this.problemId = problemId;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
