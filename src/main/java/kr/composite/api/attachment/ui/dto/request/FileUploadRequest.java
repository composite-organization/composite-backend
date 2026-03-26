package kr.composite.api.attachment.ui.dto.request;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        String originalFileName,
        String contentType,
        Long size,
        InputStream inputStream
) {

    public static FileUploadRequest from(MultipartFile file) {
        try {
            return new FileUploadRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getInputStream()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 스트림을 읽는 중 오류가 발생했습니다.", e);
        }
    }
}
