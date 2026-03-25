package kr.composite.api.participant.ui;

import kr.composite.api.exception.domain.BusinessException;

public class ParticipantUIException extends BusinessException {

    private static final String CODE_PREFIX = "PARTICIPANT_UI_";

    private ParticipantUIException(String code, String message, Category category) {
        super(code, message, category);
    }

    private ParticipantUIException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static ParticipantUIException authRequired() {
        return new ParticipantUIException(
                generateCode(1),
                "인증 정보가 필요한 요청입니다.",
                Category.UNAUTHORIZED
        );
    }

    public static ParticipantUIException participantNotFound(Long userId) {
        return new ParticipantUIException(
                generateCode(2),
                "참여자 정보가 존재하지 않습니다.",
                "제공된 사용자 ID: " + userId,
                Category.NOT_FOUND
        );
    }

    public static ParticipantUIException studentNotFound(Long participantId) {
        return new ParticipantUIException(
                generateCode(3),
                "해당 참여자는 학생이 아닙니다.",
                "제공된 참여자 ID: " + participantId,
                Category.NOT_FOUND
        );
    }

    public static ParticipantUIException teacherNotFound(Long participantId) {
        return new ParticipantUIException(
                generateCode(4),
                "해당 참여자는 수업자가 아닙니다.",
                "제공된 참여자 ID: " + participantId,
                Category.NOT_FOUND
        );
    }

    public static ParticipantUIException unsupportedParticipantType(String typeName) {
        return new ParticipantUIException(
                generateCode(5),
                "지원하지 않는 참여자 타입입니다.",
                "요청된 타입: " + typeName,
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}