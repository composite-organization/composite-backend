-- 1. url 컬럼 삭제
ALTER TABLE attachment DROP COLUMN url;

-- 2. attachment_key 컬럼 추가
ALTER TABLE attachment ADD COLUMN attachment_key VARCHAR(255) NOT NULL AFTER attachment_widget_id;

-- 3. size 타입을 BIGINT로 변경
ALTER TABLE attachment MODIFY COLUMN size BIGINT;
