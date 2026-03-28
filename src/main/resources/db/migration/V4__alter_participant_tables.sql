-- @formatter:off
-- 1. 공통 participant 테이블 생성
CREATE TABLE IF NOT EXISTS participant
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    lesson_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 2. student 테이블 컬럼 수정
ALTER TABLE student DROP COLUMN lesson_id;
ALTER TABLE student DROP COLUMN name;
ALTER TABLE student ADD COLUMN participant_id BIGINT NOT NULL;

-- 3. teacher 테이블 컬럼 수정
ALTER TABLE teacher DROP COLUMN lesson_id;
ALTER TABLE teacher DROP COLUMN name;
ALTER TABLE teacher ADD COLUMN participant_id BIGINT NOT NULL;
