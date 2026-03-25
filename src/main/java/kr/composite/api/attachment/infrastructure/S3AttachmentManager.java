package kr.composite.api.attachment.infrastructure;

import java.io.InputStream;
import java.time.Duration;
import kr.composite.api.attachment.domain.AttachmentStorage;
import kr.composite.api.attachment.domain.AttachmentUriProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

public class S3AttachmentManager implements AttachmentStorage, AttachmentUriProvider {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3AttachmentManager(
            S3Client s3Client,
            S3Presigner s3Presigner,
            String bucketName
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    @Override
    public void upload(InputStream inputStream, String key, String contentType, Long size) {

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));

        } catch (S3Exception s3Exception) {
            throw new IllegalArgumentException(s3Exception.getMessage());
        } catch (SdkClientException sdkClientException) {
            throw new IllegalArgumentException();
        } catch (Exception exception) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getReadUri(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }

    @Override
    public void deleteAttachment(String attachmentKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(attachmentKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new IllegalArgumentException("S3 파일 삭제 중 오류가 발생했습니다.");
        } catch (SdkClientException e) {
            throw new IllegalArgumentException("S3 클라이언트 연결에 문제가 발생했습니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
