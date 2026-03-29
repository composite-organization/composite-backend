package kr.composite.api.attachment.infrastructure;

import java.io.InputStream;
import java.time.Duration;
import kr.composite.api.attachment.domain.Attachment;
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
    private final String keyPrefix;

    public S3AttachmentManager(
            S3Client s3Client,
            S3Presigner s3Presigner,
            String bucketName,
            String keyPrefix
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void upload(InputStream inputStream, String key, String contentType, Long size) {

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyPrefix + key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));

        } catch (S3Exception s3Exception) {
            throw AttachmentInfrastructureException.s3UploadFailed(s3Exception.getMessage());
        } catch (SdkClientException sdkClientException) {
            throw AttachmentInfrastructureException.sdkClientError(sdkClientException.getMessage());
        } catch (Exception exception) {
            throw AttachmentInfrastructureException.unknownInfrastructureError();
        }
    }

    @Override
    public String getUri(Attachment attachment) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyPrefix + attachment.getAttachmentKey())
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }

    @Override
    public void deleteAttachment(Attachment attachment) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyPrefix + attachment.getAttachmentKey())
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw AttachmentInfrastructureException.s3DeleteFailed(e.getMessage());
        } catch (SdkClientException e) {
            throw AttachmentInfrastructureException.sdkClientError(e.getMessage());
        } catch (Exception e) {
            throw AttachmentInfrastructureException.unknownInfrastructureError();
        }
    }
}
