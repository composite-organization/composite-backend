package kr.composite.api.attachment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import jakarta.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.util.List;
import kr.composite.api.attachment.application.dto.request.AttachmentDeleteRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentFindRequest;
import kr.composite.api.attachment.application.dto.request.AttachmentWidgetFindRequest;
import kr.composite.api.attachment.application.dto.response.AttachmentMetaDataResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentResponse;
import kr.composite.api.attachment.application.dto.response.AttachmentUriResponse;
import kr.composite.api.attachment.domain.Attachment;
import kr.composite.api.attachment.domain.AttachmentName;
import kr.composite.api.attachment.domain.AttachmentRepository;
import kr.composite.api.attachment.domain.AttachmentSize;
import kr.composite.api.attachment.domain.AttachmentStorage;
import kr.composite.api.attachment.domain.AttachmentUnit;
import kr.composite.api.attachment.domain.AttachmentUriProvider;
import kr.composite.api.attachment.domain.AttachmentWidget;
import kr.composite.api.attachment.domain.AttachmentWidgetRepository;
import kr.composite.api.attachment.ui.dto.request.FileUploadRequest;
import kr.composite.api.lesson.domain.Lesson;
import kr.composite.api.lesson.domain.LessonCode;
import kr.composite.api.lesson.domain.LessonName;
import kr.composite.api.lesson.domain.LessonPassword;
import kr.composite.api.student.domain.Student;
import kr.composite.api.student.domain.StudentName;
import kr.composite.api.teacher.domain.Teacher;
import kr.composite.api.teacher.domain.TeacherName;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.domain.UserName;
import kr.composite.api.widget.domain.Widget;
import kr.composite.api.widget.domain.WidgetRepository;
import kr.composite.api.widget.domain.WidgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AttachmentServiceTest {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentWidgetRepository attachmentWidgetRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private AttachmentStorage attachmentStorage;

    @MockitoBean
    private AttachmentUriProvider attachmentUriProvider;

    @Value("${external.aws.s3.attachment.key.prefix}")
    private String keyPrefix;

    private User user;
    private Lesson lesson;
    private AttachmentWidget attachmentWidget;

    @BeforeEach
    void setUp() {
        user = new User(new UserName("testUser"));
        entityManager.persist(user);

        lesson = new Lesson(new LessonName("lesson"), new LessonCode("code"), new LessonPassword("pass123"));
        entityManager.persist(lesson);

        Widget widget = new Widget(lesson.getId(), WidgetType.ATTACHMENT);
        widgetRepository.save(widget);

        attachmentWidget = new AttachmentWidget(widget.getId());
        attachmentWidgetRepository.save(attachmentWidget);
    }

    private void makeUserTeacherOfLesson() {
        Teacher teacher = new Teacher(user.getId(), lesson.getId(), new TeacherName("teacher"));
        entityManager.persist(teacher);
    }

    private void makeUserStudentOfLesson() {
        Student student = new Student(user.getId(), lesson.getId(), new StudentName("student"));
        entityManager.persist(student);
    }

    @Test
    @DisplayName("선생님인 경우 파일을 업로드할 수 있다")
    void 선생님인_경우_파일을_업로드하면_저장소에_업로드하고_DB에_저장할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        String fileName = "test-file.png";
        String contentType = "image/png";
        byte[] content = "test content".getBytes();

        FileUploadRequest fileUploadRequest = new FileUploadRequest(
                fileName,
                contentType,
                (long) content.length,
                new ByteArrayInputStream(content)
        );

        // when
        AttachmentResponse response = attachmentService.addAttachment(user, request, fileUploadRequest);

        // then
        Attachment saved = attachmentRepository.findById(response.id()).orElseThrow();
        assertAll(
                () -> assertThat(saved.getAttachmentKey()).contains("."),
                () -> assertThat(saved.getAttachmentName().getValue()).isEqualTo(fileName),
                () -> assertThat(saved.getUnit()).isEqualTo(AttachmentUnit.KB)
        );
    }

    @Test
    @DisplayName("선생님이 아닌 경우 파일 업로드 시 예외가 발생한다")
    void 선생님이_아닌_경우_파일_업로드_예외_테스트() {
        // given
        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());
        FileUploadRequest fileUploadRequest = new FileUploadRequest("f", "c", 1L, new ByteArrayInputStream(new byte[1]));

        // when & then
        assertThatThrownBy(() -> attachmentService.addAttachment(user, request, fileUploadRequest))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    @Test
    @DisplayName("학생인 경우 등록된 모든 자료 메타데이터를 조회할 수 있다")
    void 학생인_경우_위젯_아이디로_등록된_모든_자료_메타데이터를_조회할_수_있다() {
        // given
        makeUserStudentOfLesson();
        saveAttachment(attachmentWidget.getId(), "key1", "file1.txt");
        saveAttachment(attachmentWidget.getId(), "key2", "file2.txt");

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when
        List<AttachmentMetaDataResponse> responses = attachmentService.getAttachmentMetaData(user, request);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("name")
                .containsExactlyInAnyOrder("file1.txt", "file2.txt");
    }

    @Test
    @DisplayName("참여자가 아닌 경우 자료 메타데이터 조회 시 예외가 발생한다")
    void 참여자가_아닌_경우_자료_메타데이터_조회_예외_테스트() {
        // given
        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidget.getId());

        // when & then
        assertThatThrownBy(() -> attachmentService.getAttachmentMetaData(user, request))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    @Test
    @DisplayName("선생님인 경우 자료 상세 조회 시 URL을 반환받을 수 있다")
    void 선생님인_경우_자료_상세_조회_시_Presigned_URL을_반환받을_수_있다() {
        // given
        makeUserTeacherOfLesson();
        Attachment attachment = saveAttachment(attachmentWidget.getId(), "s3-storage-key", "report.pdf");

        String expectedUrl = "https://s3.amazonaws.com/presigned-url-example";
        given(attachmentUriProvider.getUri(attachment)).willReturn(expectedUrl);

        AttachmentFindRequest request = new AttachmentFindRequest(attachment.getId(), attachmentWidget.getId());

        // when
        AttachmentUriResponse response = attachmentService.getAttachment(user, request);

        // then
        assertThat(response.presignedUrl()).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("선생님인 경우 자료를 삭제할 수 있다")
    void 선생님인_경우_자료를_삭제할_수_있다() {
        // given
        makeUserTeacherOfLesson();
        Attachment attachment = saveAttachment(attachmentWidget.getId(), "s3-storage-key", "report.pdf");
        AttachmentDeleteRequest attachmentDeleteRequest = AttachmentDeleteRequest.of(
                attachment.getId(),
                attachmentWidget.getId());

        // when
        attachmentService.deleteAttachment(user, attachmentDeleteRequest);

        // then
        assertThat(attachmentRepository.findById(attachment.getId())).isEmpty();
    }

    @Test
    @DisplayName("선생님이 아닌 경우 자료 삭제 시 예외가 발생한다")
    void 선생님이_아닌_경우_자료_삭제_예외_테스트() {
        // given
        Attachment attachment = saveAttachment(attachmentWidget.getId(), "s3-storage-key", "report.pdf");
        AttachmentDeleteRequest attachmentDeleteRequest = AttachmentDeleteRequest.of(
                attachment.getId(),
                attachmentWidget.getId());

        // when & then
        assertThatThrownBy(() -> attachmentService.deleteAttachment(user, attachmentDeleteRequest))
                .isInstanceOf(AttachmentApplicationException.class);
    }

    private Attachment saveAttachment(Long attachmentWidgetId, String key, String name) {
        Attachment attachment = new Attachment(
                attachmentWidgetId,
                key,
                new AttachmentName(name),
                new AttachmentSize(1024L),
                AttachmentUnit.KB
        );

        return attachmentRepository.save(attachment);
    }
}
