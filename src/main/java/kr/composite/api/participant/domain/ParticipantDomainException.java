package kr.composite.api.participant.domain;

import kr.composite.api.exception.domain.BusinessException;

class ParticipantDomainException extends BusinessException {

    private static final String CODE_PREFIX = "PARTICIPANT_DOMAIN_";

    private ParticipantDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private ParticipantDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static ParticipantDomainException emptyName() {
        return new ParticipantDomainException(
                generateCode(1),
                "참여자 이름이 비어있습니다.",
                Category.VALIDATION
        );
    }

    public static ParticipantDomainException invalidNameLength(String name, int minLength, int maxLength) {
        return new ParticipantDomainException(
                generateCode(2),
                "참여자 이름의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 이름: " + name + " (길이: " + name.length() + ")",
                Category.VALIDATION
        );
    }

    public static ParticipantDomainException participantNotFound() {
        return new ParticipantDomainException(
                generateCode(3),
                "참여자 정보를 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}