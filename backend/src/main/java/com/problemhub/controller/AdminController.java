package com.problemhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final JdbcTemplate jdbcTemplate;

    public AdminController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Long totalProblems = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM problems", Long.class);
        Long publishedProblems = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM problems WHERE status = 'PUBLISHED'", Long.class);
        Long archivedProblems = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM problems WHERE status = 'ARCHIVED'", Long.class);
        Long draftProblems = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM problems WHERE status = 'DRAFT'", Long.class);
        Long totalUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        Long totalStudents = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE role = 'STUDENT'", Long.class);
        Long totalBookmarks = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bookmarks", Long.class);
        Long totalTechnologies = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM technologies", Long.class);
        Long totalTags = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tags", Long.class);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalProblems", totalProblems != null ? totalProblems : 0);
        stats.put("publishedProblems", publishedProblems != null ? publishedProblems : 0);
        stats.put("archivedProblems", archivedProblems != null ? archivedProblems : 0);
        stats.put("draftProblems", draftProblems != null ? draftProblems : 0);
        stats.put("totalUsers", totalUsers != null ? totalUsers : 0);
        stats.put("totalStudents", totalStudents != null ? totalStudents : 0);
        stats.put("totalBookmarks", totalBookmarks != null ? totalBookmarks : 0);
        stats.put("totalTechnologies", totalTechnologies != null ? totalTechnologies : 0);
        stats.put("totalTags", totalTags != null ? totalTags : 0);

        return ResponseEntity.ok(stats);
    }
}
