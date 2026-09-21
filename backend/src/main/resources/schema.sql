-- Problem Hub Database Schema
-- Strict Relational MySQL Schema for Spring JDBC / JdbcTemplate

CREATE DATABASE IF NOT EXISTS problemhub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE problemhub;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Problems Table
CREATE TABLE IF NOT EXISTS problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    domain VARCHAR(100) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    project_type VARCHAR(30) NOT NULL,
    impact TEXT,
    solution_direction TEXT,
    expected_outcome TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_problems_domain (domain),
    INDEX idx_problems_difficulty (difficulty),
    INDEX idx_problems_project_type (project_type),
    INDEX idx_problems_status (status),
    INDEX idx_problems_created_at (created_at),
    FULLTEXT INDEX idx_problems_search (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Technologies Table
CREATE TABLE IF NOT EXISTS technologies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_tech_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Tags Table
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_tags_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Problem Technologies Mapping Table
CREATE TABLE IF NOT EXISTS problem_technologies (
    problem_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    PRIMARY KEY (problem_id, technology_id),
    FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE,
    FOREIGN KEY (technology_id) REFERENCES technologies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Problem Tags Mapping Table
CREATE TABLE IF NOT EXISTS problem_tags (
    problem_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (problem_id, tag_id),
    FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Bookmarks Table
CREATE TABLE IF NOT EXISTS bookmarks (
    user_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, problem_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE,
    INDEX idx_bookmarks_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
