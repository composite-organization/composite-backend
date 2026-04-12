MERGE INTO student (id, user_id, lesson_id, name, created_at, updated_at, deleted)
KEY (id)
VALUES (1, 1, 1, '테스트학생', NOW(), NOW(), 0);
