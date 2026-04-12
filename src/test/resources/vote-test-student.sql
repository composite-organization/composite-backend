MERGE INTO participant (id, user_id, lesson_id, name, created_at, updated_at, deleted)
KEY (id)
VALUES (1, 1, 1, '테스트학생', NOW(), NOW(), 0);

MERGE INTO student (id, participant_id, created_at, updated_at, deleted)
KEY (id)
VALUES (1, 1, NOW(), NOW(), 0);
