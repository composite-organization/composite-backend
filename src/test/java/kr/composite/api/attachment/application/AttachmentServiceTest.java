package kr.composite.api.attachment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

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
import kr.composite.api.attachment.ui.dto.request.FileUploadRequest;
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

    @MockitoBean
    private AttachmentStorage attachmentStorage;

    @MockitoBean
    private AttachmentUriProvider attachmentUriProvider;

    @Value("${external.aws.s3.attachment.key.prefix}")
    private String keyPrefix;

    @Test
    void 파일을_업로드하면_저장소에_업로드하고_DB에_저장할_수_있다() {
        // given
        Long widgetId = 1L;
        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(widgetId);

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
        AttachmentResponse response = attachmentService.addAttachment(request, fileUploadRequest);

        // then
        Attachment saved = attachmentRepository.findById(response.id()).orElseThrow();
        assertAll(
                () -> assertThat(saved.getAttachmentKey()).contains("."),
                () -> assertThat(saved.getAttachmentName().getValue()).isEqualTo(fileName),
                () -> assertThat(saved.getUnit()).isEqualTo(AttachmentUnit.KB)
        );
    }

    @Test
    void 위젯_아이디로_등록된_모든_자료_메타데이터를_조회할_수_있다() {
        // given
        Long attachmentWidgetId = 100L;
        saveAttachment(attachmentWidgetId, "key1", "file1.txt");
        saveAttachment(attachmentWidgetId, "key2", "file2.txt");

        AttachmentWidgetFindRequest request = AttachmentWidgetFindRequest.from(attachmentWidgetId);

        // when
        List<AttachmentMetaDataResponse> responses = attachmentService.readAttachmentMetaData(request);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("name")
                .containsExactlyInAnyOrder("file1.txt", "file2.txt");
    }

    @Test
    void 자료_상세_조회_시_S3로부터_생성된_Presigned_URL을_반환받을_수_있다() {
        // given
        Long attachmentWidgetId = 1L;
        Attachment attachment = saveAttachment(attachmentWidgetId, "s3-storage-key", "report.pdf");

        String expectedUrl = "https://s3.amazonaws.com/presigned-url-example";
        given(attachmentUriProvider.getUri(attachment)).willReturn(expectedUrl);

        AttachmentFindRequest request = new AttachmentFindRequest(attachment.getId(), attachmentWidgetId);

        // when
        AttachmentUriResponse response = attachmentService.readAttachment(request);

        // then
        assertThat(response.presignedUrl()).isEqualTo(expectedUrl);
    }

    @Test
    void 자료를_삭제할_수_있다() {
        // given
        Long attachmentWidgetId = 1L;
        Attachment attachment = saveAttachment(attachmentWidgetId, "s3-storage-key", "report.pdf");
        AttachmentDeleteRequest attachmentDeleteRequest = AttachmentDeleteRequest.of(
                attachment.getId(),
                attachmentWidgetId);

        // when
        attachmentService.deleteAttachment(attachmentDeleteRequest);

        // then
        assertThat(attachmentRepository.findById(attachment.getId())).isEmpty();
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
