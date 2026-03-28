package kr.composite.api.lesson.application;

import java.util.Optional;
import kr.composite.api.lesson.ui.dto.request.JoinLessonRequest;
import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.ParticipantName;
import kr.composite.api.participant.domain.ParticipantRepository;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentParticipateEvent;
import kr.composite.api.participant.domain.StudentRepository;
import kr.composite.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final StudentRepository studentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void joinStudent(Long lessonId, User user, JoinLessonRequest request) {
        if (participantRepository.existsByLessonIdAndUserId(lessonId, user.getId())) {
            throw LessonApplicationException.alreadyJoined();
        }
        ParticipantName participantName = new ParticipantName(
                Optional.ofNullable(request.name()).orElse(user.getName().getValue())
        );
        Participant participantToSave = new Participant(user.getId(), lessonId, participantName);
        Participant savedParticipant = participantRepository.save(participantToSave);
        Student studentToSave = new Student(savedParticipant.getId());
        Student savedStudent = studentRepository.save(studentToSave);

        eventPublisher.publishEvent(new StudentParticipateEvent(savedStudent, lessonId));
    }
}
