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

    public static ParticipantUIException unsupportedParticipantType() {
        return new ParticipantUIException(
                generateCode(5),
                "지원하지 않는 참여자 타입입니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
