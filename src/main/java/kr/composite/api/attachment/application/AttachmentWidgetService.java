package kr.composite.api.attachment.application;

import kr.composite.api.attachment.application.dto.request.AttachmentWidgetAddRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentWidgetResponse;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.student.domain.StudentRepository;
import kr.composite.api.teacher.domain.TeacherRepository;
import kr.composite.api.user.domain.User;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachmentWidgetService {

    private final AttachmentWidgetRepository attachmentWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public AttachmentWidgetResponse getAttachmentWidget(User user, AttachmentWidgetFindRequest request) {
        AttachmentWidget attachmentWidget = attachmentWidgetRepository.findById(request.id())
                .orElseThrow(AttachmentApplicationException::widgetNotFound);

        Widget widget = widgetRepository.findById(attachmentWidget.getWidgetId())
                .orElseThrow(AttachmentApplicationException::widgetNotFound);

        validateParticipant(widget.getLessonId(), user.getId());

        AttachmentWidgetResponse attachmentWidgetResponse = AttachmentWidgetResponse.from(attachmentWidget);

        return attachmentWidgetResponse;
    }

    @Transactional
    public AttachmentWidgetResponse addAttachmentWidget(User user, AttachmentWidgetAddRequest request) {
        validateTeacher(request.lessonId(), user.getId());

        Widget widget = new Widget(request.lessonId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);

        AttachmentWidgetResponse attachmentWidgetResponse = AttachmentWidgetResponse.from(attachmentWidget);

        return attachmentWidgetResponse;
    }

    // TODO: 자료공유 위젯 삭제 시 자료 삭제 여부
    @Transactional
    public void deleteAttachmentWidget(User user, AttachmentWidgetDeleteRequest request) {
        AttachmentWidget attachmentWidget = attachmentWidgetRepository.findById(request.id())
                .orElseThrow(AttachmentApplicationException::widgetNotFound);

        Widget widget = widgetRepository.findById(attachmentWidget.getWidgetId())
                .orElseThrow(AttachmentApplicationException::widgetNotFound);

        validateTeacher(widget.getLessonId(), user.getId());

        widgetRepository.deleteById(attachmentWidget.getWidgetId());
        attachmentWidgetRepository.deleteById(attachmentWidget.getId());
    }

    private void validateTeacher(Long lessonId, Long userId) {
        if (!teacherRepository.existsByLessonIdAndUserId(lessonId, userId)) {
            throw AttachmentApplicationException.forbidden(); // Assuming forbidden exists or will be added
        }
    }

    private void validateParticipant(Long lessonId, Long userId) {
        boolean isTeacher = teacherRepository.existsByLessonIdAndUserId(lessonId, userId);
        boolean isStudent = studentRepository.existsByLessonIdAndUserId(lessonId, userId);

        if (!isTeacher && !isStudent) {
            throw AttachmentApplicationException.forbidden();
        }
    }
}
