package com.problemhub.repository;

import com.problemhub.model.Technology;
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
public class TechnologyRepository {

    private final JdbcTemplate jdbcTemplate;

    public TechnologyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Technology> rowMapper = (rs, rowNum) ->
            new Technology(rs.getLong("id"), rs.getString("name"));

    public List<Technology> findAll() {
        String sql = "SELECT id, name FROM technologies ORDER BY name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Technology> findById(Long id) {
        String sql = "SELECT id, name FROM technologies WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Technology> findByName(String name) {
        String sql = "SELECT id, name FROM technologies WHERE LOWER(name) = LOWER(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name.trim()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Technology> findByProblemId(Long problemId) {
        String sql = """
            SELECT t.id, t.name
            FROM technologies t
            INNER JOIN problem_technologies pt ON t.id = pt.technology_id
            WHERE pt.problem_id = ?
            ORDER BY t.name ASC
        """;
        return jdbcTemplate.query(sql, rowMapper, problemId);
    }

    public Map<Long, List<Technology>> findByProblemIds(List<Long> problemIds) {
        if (problemIds == null || problemIds.isEmpty()) {
            return Collections.emptyMap();
        }
        String inSql = String.join(",", Collections.nCopies(problemIds.size(), "?"));
        String sql = String.format("""
            SELECT pt.problem_id, t.id, t.name
            FROM technologies t
            INNER JOIN problem_technologies pt ON t.id = pt.technology_id
            WHERE pt.problem_id IN (%s)
            ORDER BY t.name ASC
        """, inSql);

        Map<Long, List<Technology>> map = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            long problemId = rs.getLong("problem_id");
            Technology tech = new Technology(rs.getLong("id"), rs.getString("name"));
            map.computeIfAbsent(problemId, k -> new ArrayList<>()).add(tech);
        }, problemIds.toArray());
        return map;
    }

    public Technology save(Technology technology) {
        String sql = "INSERT INTO technologies (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, technology.getName().trim());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            technology.setId(key.longValue());
        }
        return technology;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM technologies WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM technologies WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name.trim());
        return count != null && count > 0;
    }
}
