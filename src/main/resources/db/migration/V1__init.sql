CREATE TABLE IF NOT EXISTS lesson (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    code VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    lesson_id BIGINT NULL,
    type VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS memo_widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    widget_id BIGINT NULL,
    title VARCHAR(255) NULL,
    content VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS attachment_widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    widget_id BIGINT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS attachment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    attachment_widget_id BIGINT NULL,
    url VARCHAR(255) NULL,
    name VARCHAR(255) NULL,
    size INT NULL,
    unit VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS question_widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    widget_id BIGINT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS student (
    id BIGINT NOT NULL AUTO_INCREMENT,
    lesson_id BIGINT NULL,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS teacher (
    id BIGINT NOT NULL AUTO_INCREMENT,
    lesson_id BIGINT NULL,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS question (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NULL,
    question_widget_id BIGINT NULL,
    content VARCHAR(255) NULL,
    is_anonymous BOOLEAN NOT NULL,
    is_completed BOOLEAN NOT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS quiz_widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    widget_id BIGINT NULL,
    title VARCHAR(255) NULL,
    status VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS quiz_option (
    id BIGINT NOT NULL AUTO_INCREMENT,
    quiz_widget_id BIGINT NULL,
    content VARCHAR(255) NULL,
    is_correct BOOLEAN NOT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS quiz_submission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NULL,
    quiz_widget_id BIGINT NULL,
    quiz_option_id BIGINT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS vote_widget (
    id BIGINT NOT NULL AUTO_INCREMENT,
    widget_id BIGINT NULL,
    title VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    is_anonymous BOOLEAN NOT NULL,
    is_multi_selectable BOOLEAN NOT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS vote_option (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vote_widget_id BIGINT NULL,
    content VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS vote_submission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NULL,
    vote_widget_id BIGINT NULL,
    vote_option_id BIGINT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS guest (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
