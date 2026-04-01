-- 1. student 테이블 컬럼 추가
ALTER TABLE student ADD COLUMN lesson_id BIGINT;
ALTER TABLE student ADD COLUMN user_id BIGINT;
ALTER TABLE student ADD COLUMN name VARCHAR(255);

-- 2. teacher 테이블 컬럼 추가
ALTER TABLE teacher ADD COLUMN lesson_id BIGINT;
ALTER TABLE teacher ADD COLUMN user_id BIGINT;
ALTER TABLE teacher ADD COLUMN name VARCHAR(255);

ALTER TABLE student MODIFY COLUMN lesson_id BIGINT NOT NULL;
ALTER TABLE student MODIFY COLUMN user_id BIGINT NOT NULL;
ALTER TABLE teacher MODIFY COLUMN lesson_id BIGINT NOT NULL;
ALTER TABLE teacher MODIFY COLUMN user_id BIGINT NOT NULL;

-- 3. participant_id 컬럼 삭제
ALTER TABLE student DROP COLUMN participant_id;
ALTER TABLE teacher DROP COLUMN participant_id;

-- 4. participant 테이블 삭제
DROP TABLE IF EXISTS participant;
