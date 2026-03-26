package kr.composite.api.attachment.infrastructure;

import kr.composite.api.attachment.domain.AttachmentStorage;
import kr.composite.api.attachment.domain.AttachmentUriProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AttachmentStorageConfiguration {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public S3AttachmentManager s3AttachmentManager(
            @Value("${external.aws.s3.attachment.bucket.name}") String bucketName,
            @Value("${external.aws.s3.attachment.key.prefix}") String keyPrefix,
            S3Client s3Client,
            S3Presigner s3Presigner
    ) {
        return new S3AttachmentManager(s3Client, s3Presigner, bucketName, keyPrefix);
    }

    @Bean
    public AttachmentStorage attachmentStorage(S3AttachmentManager s3AttachmentManager) {
        return s3AttachmentManager;
    }

    @Bean
    public AttachmentUriProvider attachmentUriProvider(S3AttachmentManager s3AttachmentManager) {
        return s3AttachmentManager;
    }

    @Bean
    public S3Client s3Client(@Value("${external.aws.s3.region}") String regionName) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(@Value("${external.aws.s3.region}") String regionName) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
