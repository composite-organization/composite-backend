package kr.composite.api.attachment.infrastructure;

import kr.composite.api.exception.domain.BusinessException;

class AttachmentInfrastructureException extends BusinessException {

    private static final String CODE_PREFIX = "ATTACHMENT_INFRA_";

    private AttachmentInfrastructureException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static AttachmentInfrastructureException s3UploadFailed(String detail) {
        return new AttachmentInfrastructureException(
                generateCode(1),
                "S3 파일 업로드에 실패했습니다.",
                detail,
                Category.EXTERNAL_SERVICE_ERROR
        );
    }

    public static AttachmentInfrastructureException s3DeleteFailed(String detail) {
        return new AttachmentInfrastructureException(
                generateCode(2),
                "S3 파일 삭제에 실패했습니다.",
                detail,
                Category.EXTERNAL_SERVICE_ERROR
        );
    }

    public static AttachmentInfrastructureException sdkClientError(String detail) {
        return new AttachmentInfrastructureException(
                generateCode(3),
                "SDK 클라이언트 오류가 발생했습니다.",
                detail,
                Category.EXTERNAL_SERVICE_ERROR
        );
    }

    public static AttachmentInfrastructureException unknownInfrastructureError() {
        return new AttachmentInfrastructureException(
                generateCode(4),
                "알 수 없는 인프라 오류가 발생했습니다.",
                "스택 트레이스를 확인해주세요.",
                Category.INTERNAL_SERVER_ERROR
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
