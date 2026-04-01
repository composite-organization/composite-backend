package kr.composite.api.lesson.infrastructure;

import java.util.Optional;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import kr.composite.api.lesson.domain.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLessonRepository implements LessonRepository {

    private final SpringDataJpaLessonRepository springDataJpaLessonRepository;

    @Override
    public void save(Lesson lesson) {
        springDataJpaLessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> findByLessonCode(LessonCode lessonCode) {
        return springDataJpaLessonRepository.findLessonByCode(lessonCode);
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        return springDataJpaLessonRepository.findById(id);
    }
}
