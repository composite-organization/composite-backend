-- @formatter:off
-- 1. user 테이블 신규 생성 (User 엔티티 대응)
CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. guest 테이블 컬럼 수정 (user_id를 FK로 추가)
ALTER TABLE guest DROP COLUMN name;
ALTER TABLE guest ADD COLUMN user_id BIGINT NOT NULL;
ALTER TABLE guest ADD CONSTRAINT fk_guest_user_id FOREIGN KEY (user_id) REFERENCES user (id);

-- 3. member 테이블 컬럼 수정 (user_id를 FK로 추가)
ALTER TABLE member DROP COLUMN name;
ALTER TABLE member ADD COLUMN user_id BIGINT NOT NULL;
ALTER TABLE member ADD CONSTRAINT fk_member_user_id FOREIGN KEY (user_id) REFERENCES user (id);
