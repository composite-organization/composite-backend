package kr.composite.api.participant.application;

import kr.composite.api.exception.domain.BusinessException;

class ParticipantApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "PARTICIPANT_APPLICATION_";

    private ParticipantApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    private ParticipantApplicationException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static ParticipantApplicationException participantNotFound() {
        return new ParticipantApplicationException(
                generateCode(1),
                "참여자 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }

    public static ParticipantApplicationException studentNotFound() {
        return new ParticipantApplicationException(
                generateCode(2),
                "학생 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }

    public static ParticipantApplicationException teacherNotFound() {
        return new ParticipantApplicationException(
                generateCode(3),
                "수업자 정보가 존재하지 않습니다.",
                Category.NOT_FOUND
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
