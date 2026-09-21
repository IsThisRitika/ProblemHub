package com.problemhub.repository;

import com.problemhub.model.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class TagRepository {

    private final JdbcTemplate jdbcTemplate;

    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tag> rowMapper = (rs, rowNum) ->
            new Tag(rs.getLong("id"), rs.getString("name"));

    public List<Tag> findAll() {
        String sql = "SELECT id, name FROM tags ORDER BY name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Tag> findById(Long id) {
        String sql = "SELECT id, name FROM tags WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Tag> findByName(String name) {
        String sql = "SELECT id, name FROM tags WHERE LOWER(name) = LOWER(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name.trim()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Tag> findByProblemId(Long problemId) {
        String sql = """
            SELECT t.id, t.name
            FROM tags t
            INNER JOIN problem_tags pt ON t.id = pt.tag_id
            WHERE pt.problem_id = ?
            ORDER BY t.name ASC
        """;
        return jdbcTemplate.query(sql, rowMapper, problemId);
    }

    public Map<Long, List<Tag>> findByProblemIds(List<Long> problemIds) {
        if (problemIds == null || problemIds.isEmpty()) {
            return Collections.emptyMap();
        }
        String inSql = String.join(",", Collections.nCopies(problemIds.size(), "?"));
        String sql = String.format("""
            SELECT pt.problem_id, t.id, t.name
            FROM tags t
            INNER JOIN problem_tags pt ON t.id = pt.tag_id
            WHERE pt.problem_id IN (%s)
            ORDER BY t.name ASC
        """, inSql);

        Map<Long, List<Tag>> map = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            long problemId = rs.getLong("problem_id");
            Tag tag = new Tag(rs.getLong("id"), rs.getString("name"));
            map.computeIfAbsent(problemId, k -> new ArrayList<>()).add(tag);
        }, problemIds.toArray());
        return map;
    }

    public Tag save(Tag tag) {
        String sql = "INSERT INTO tags (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName().trim());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            tag.setId(key.longValue());
        }
        return tag;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM tags WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM tags WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.trim());
        return count != null && count > 0;
    }
}
