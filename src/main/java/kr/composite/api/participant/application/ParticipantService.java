package kr.composite.api.participant.application;

import kr.composite.api.participant.domain.Participant;
import kr.composite.api.participant.domain.ParticipantRepository;
import kr.composite.api.participant.domain.Student;
import kr.composite.api.participant.domain.StudentRepository;
import kr.composite.api.participant.domain.Teacher;
import kr.composite.api.participant.domain.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Participant getParticipant(Long userId) {
        return participantRepository.findByUserId(userId)
                .orElseThrow(ParticipantApplicationException::participantNotFound);
    }

    public Student getStudent(Participant participant) {
        return studentRepository.findByParticipantId(participant.getId())
                .orElseThrow(ParticipantApplicationException::studentNotFound);
    }

    public Teacher getTeacher(Participant participant) {
        return teacherRepository.findByParticipantId(participant.getId())
                .orElseThrow(ParticipantApplicationException::teacherNotFound);
    }
}
