package com.problemhub.repository;

import com.problemhub.model.Problem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ProblemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProblemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Problem> rowMapper = (rs, rowNum) -> {
        Problem p = new Problem();
        p.setId(rs.getLong("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setDomain(rs.getString("domain"));
        p.setDifficulty(rs.getString("difficulty"));
        p.setProjectType(rs.getString("project_type"));
        p.setImpact(rs.getString("impact"));
        p.setSolutionDirection(rs.getString("solution_direction"));
        p.setExpectedOutcome(rs.getString("expected_outcome"));
        p.setStatus(rs.getString("status"));
        
        long createdBy = rs.getLong("created_by");
        if (!rs.wasNull()) {
            p.setCreatedBy(createdBy);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            p.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            p.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return p;
    };

    public Optional<Problem> findById(Long id) {
        String sql = """
            SELECT id, title, description, domain, difficulty, project_type,
                   impact, solution_direction, expected_outcome, status,
                   created_by, created_at, updated_at
            FROM problems
            WHERE id = ?
        """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Problem> findAll(int offset, int limit, String sortBy, String sortDir) {
        // Sanitize sort parameters to prevent SQL injection
        String validSortBy = switch (sortBy != null ? sortBy.toLowerCase() : "created_at") {
            case "title" -> "title";
            case "domain" -> "domain";
            case "difficulty" -> "difficulty";
            case "project_type", "projecttype" -> "project_type";
            case "status" -> "status";
            default -> "created_at";
        };
        String validSortDir = "ASC".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";

        String sql = String.format("""
            SELECT id, title, description, domain, difficulty, project_type,
                   impact, solution_direction, expected_outcome, status,
                   created_by, created_at, updated_at
            FROM problems
            ORDER BY %s %s
            LIMIT ? OFFSET ?
        """, validSortBy, validSortDir);

        return jdbcTemplate.query(sql, rowMapper, limit, offset);
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM problems";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    public Problem save(Problem problem) {
        String sql = """
            INSERT INTO problems (
                title, description, domain, difficulty, project_type,
                impact, solution_direction, expected_outcome, status, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, problem.getTitle());
            ps.setString(2, problem.getDescription());
            ps.setString(3, problem.getDomain());
            ps.setString(4, problem.getDifficulty());
            ps.setString(5, problem.getProjectType());
            ps.setString(6, problem.getImpact());
            ps.setString(7, problem.getSolutionDirection());
            ps.setString(8, problem.getExpectedOutcome());
            ps.setString(9, problem.getStatus() != null ? problem.getStatus() : "PUBLISHED");
            if (problem.getCreatedBy() != null) {
                ps.setLong(10, problem.getCreatedBy());
            } else {
                ps.setNull(10, java.sql.Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            problem.setId(key.longValue());
        }
        return problem;
    }

    public boolean update(Problem problem) {
        String sql = """
            UPDATE problems SET
                title = ?,
                description = ?,
                domain = ?,
                difficulty = ?,
                project_type = ?,
                impact = ?,
                solution_direction = ?,
                expected_outcome = ?,
                status = ?
            WHERE id = ?
        """;

        int rows = jdbcTemplate.update(
                sql,
                problem.getTitle(),
                problem.getDescription(),
                problem.getDomain(),
                problem.getDifficulty(),
                problem.getProjectType(),
                problem.getImpact(),
                problem.getSolutionDirection(),
                problem.getExpectedOutcome(),
                problem.getStatus(),
                problem.getId()
        );
        return rows > 0;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM problems WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean updateStatus(Long id, String status) {
        String sql = "UPDATE problems SET status = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, id) > 0;
    }

    public void setProblemTechnologies(Long problemId, List<Long> technologyIds) {
        // Remove old associations
        jdbcTemplate.update("DELETE FROM problem_technologies WHERE problem_id = ?", problemId);

        if (technologyIds != null && !technologyIds.isEmpty()) {
            String insertSql = "INSERT INTO problem_technologies (problem_id, technology_id) VALUES (?, ?)";
            List<Object[]> batchArgs = new ArrayList<>();
            for (Long techId : technologyIds) {
                batchArgs.add(new Object[]{problemId, techId});
            }
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }

    public void setProblemTags(Long problemId, List<Long> tagIds) {
        // Remove old associations
        jdbcTemplate.update("DELETE FROM problem_tags WHERE problem_id = ?", problemId);

        if (tagIds != null && !tagIds.isEmpty()) {
            String insertSql = "INSERT INTO problem_tags (problem_id, tag_id) VALUES (?, ?)";
            List<Object[]> batchArgs = new ArrayList<>();
            for (Long tagId : tagIds) {
                batchArgs.add(new Object[]{problemId, tagId});
            }
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }

    public String findCreatorName(Long userId) {
        if (userId == null) return null;
        String sql = "SELECT name FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
