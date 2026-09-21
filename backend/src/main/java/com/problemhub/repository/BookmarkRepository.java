package com.problemhub.repository;

import com.problemhub.model.Problem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class BookmarkRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookmarkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Problem> problemRowMapper = (rs, rowNum) -> {
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

    public boolean addBookmark(Long userId, Long problemId) {
        String sql = "INSERT INTO bookmarks (user_id, problem_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE created_at = created_at";
        return jdbcTemplate.update(sql, userId, problemId) > 0;
    }

    public boolean removeBookmark(Long userId, Long problemId) {
        String sql = "DELETE FROM bookmarks WHERE user_id = ? AND problem_id = ?";
        return jdbcTemplate.update(sql, userId, problemId) > 0;
    }

    public boolean isBookmarked(Long userId, Long problemId) {
        String sql = "SELECT COUNT(*) FROM bookmarks WHERE user_id = ? AND problem_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, problemId);
        return count != null && count > 0;
    }

    public Set<Long> findBookmarkedProblemIds(Long userId) {
        String sql = "SELECT problem_id FROM bookmarks WHERE user_id = ?";
        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, userId);
        return new HashSet<>(ids);
    }

    public Set<Long> findBookmarkedProblemIdsIn(Long userId, List<Long> problemIds) {
        if (problemIds == null || problemIds.isEmpty()) {
            return Collections.emptySet();
        }
        String inSql = String.join(",", Collections.nCopies(problemIds.size(), "?"));
        String sql = String.format("SELECT problem_id FROM bookmarks WHERE user_id = ? AND problem_id IN (%s)", inSql);

        Object[] params = new Object[problemIds.size() + 1];
        params[0] = userId;
        for (int i = 0; i < problemIds.size(); i++) {
            params[i + 1] = problemIds.get(i);
        }

        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, params);
        return new HashSet<>(ids);
    }

    public List<Problem> findBookmarkedProblems(Long userId) {
        String sql = """
            SELECT p.id, p.title, p.description, p.domain, p.difficulty, p.project_type,
                   p.impact, p.solution_direction, p.expected_outcome, p.status,
                   p.created_by, p.created_at, p.updated_at
            FROM problems p
            INNER JOIN bookmarks b ON p.id = b.problem_id
            WHERE b.user_id = ?
            ORDER BY b.created_at DESC
        """;
        return jdbcTemplate.query(sql, problemRowMapper, userId);
    }
}
