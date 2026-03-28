package kr.composite.api.attachment.ui.dto.request;

import java.io.IOException;
import java.io.InputStream;
import kr.composite.api.attachment.ui.AttachmentUIException;
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
            throw AttachmentUIException.fileStreamReadFailed();
        }
    }
}
