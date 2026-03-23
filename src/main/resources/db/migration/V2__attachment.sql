-- 1. url 컬럼 삭제
-- 2. attachment_key 컬럼 추가 (VARCHAR 255, NOT NULL)
-- 3. size 타입을 BIGINT로 변경하여 Long 타입 대응

ALTER TABLE attachment
DROP COLUMN url,
    ADD COLUMN attachment_key VARCHAR(255) NOT NULL AFTER attachment_widget_id,
    MODIFY COLUMN size BIGINT;
