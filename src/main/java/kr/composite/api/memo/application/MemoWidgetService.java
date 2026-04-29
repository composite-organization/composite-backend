package kr.composite.api.memo.application;

import kr.composite.api.memo.application.dto.request.MemoWidgetAddRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetFindRequest;
import kr.composite.api.memo.application.dto.request.MemoWidgetUpdateRequest;
import kr.composite.api.memo.application.dto.response.MemoWidgetResponse;
import kr.composite.api.memo.domain.MemoContent;
import kr.composite.api.memo.domain.MemoTitle;
import kr.composite.api.memo.domain.MemoWidget;
import kr.composite.api.memo.domain.MemoWidgetRepository;
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
public class MemoWidgetService {

    private final MemoWidgetRepository memoWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public MemoWidgetResponse getMemoWidget(User user, MemoWidgetFindRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.id())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        Widget widget = widgetRepository.findById(memoWidget.getWidgetId())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        validateParticipant(widget.getLessonId(), user.getId());

        MemoWidgetResponse memoWidgetResponse = MemoWidgetResponse.from(memoWidget);

        return memoWidgetResponse;
    }

    @Transactional
    public MemoWidgetResponse addMemoWidget(User user, MemoWidgetAddRequest request) {
        validateTeacher(request.lessonId(), user.getId());

        Widget widget = new Widget(request.lessonId(), WidgetType.MEMO);
        widgetRepository.save(widget);

        MemoTitle memoTitle = new MemoTitle(request.title());
        MemoContent memoContent = new MemoContent(request.content());
        MemoWidget memoWidget = new MemoWidget(widget.getId(), memoTitle, memoContent);
        memoWidgetRepository.save(memoWidget);

        return MemoWidgetResponse.from(memoWidget);
    }

    @Transactional
    public MemoWidgetResponse updateMemoWidget(User user, MemoWidgetUpdateRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.memoWidgetId())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        Widget widget = widgetRepository.findById(memoWidget.getWidgetId())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        validateTeacher(widget.getLessonId(), user.getId());

        memoWidget.update(request.title(), request.content());

        MemoWidgetResponse memoWidgetResponse = MemoWidgetResponse.from(memoWidget);

        return memoWidgetResponse;
    }

    @Transactional
    public void deleteMemoWidget(User user, MemoWidgetFindRequest request) {
        MemoWidget memoWidget = memoWidgetRepository.findById(request.id())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        Widget widget = widgetRepository.findById(memoWidget.getWidgetId())
                .orElseThrow(MemoApplicationException::widgetNotFound);

        validateTeacher(widget.getLessonId(), user.getId());

        memoWidgetRepository.deleteById(request.id());
        widgetRepository.deleteById(memoWidget.getWidgetId());
    }

    private void validateTeacher(Long lessonId, Long userId) {
        if (!teacherRepository.existsByLessonIdAndUserId(lessonId, userId)) {
            throw MemoApplicationException.forbidden();
        }
    }

    private void validateParticipant(Long lessonId, Long userId) {
        boolean isTeacher = teacherRepository.existsByLessonIdAndUserId(lessonId, userId);
        boolean isStudent = studentRepository.existsByLessonIdAndUserId(lessonId, userId);

        if (!isTeacher && !isStudent) {
            throw MemoApplicationException.forbidden();
        }
    }
}
