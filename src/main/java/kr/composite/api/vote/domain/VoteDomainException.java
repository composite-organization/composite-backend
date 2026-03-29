package kr.composite.api.vote.domain;

import kr.composite.api.exception.domain.BusinessException;

class VoteDomainException extends BusinessException {

    private static final String CODE_PREFIX = "VOTE_DOMAIN_";

    private VoteDomainException(String code, String message, Category category) {
        super(code, message, category);
    }

    private VoteDomainException(String code, String message, String detail, Category category) {
        super(code, message, detail, category);
    }

    public static VoteDomainException emptyTitle() {
        return new VoteDomainException(
                generateCode(1),
                "투표 제목이 비어있습니다.",
                null,
                Category.VALIDATION
        );
    }

    public static VoteDomainException invalidTitleLength(String title, int minLength, int maxLength) {
        return new VoteDomainException(
                generateCode(2),
                "투표 제목의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 제목: " + title + " (길이: " + title.length() + ")",
                Category.VALIDATION
        );
    }

    public static VoteDomainException emptyDescription() {
        return new VoteDomainException(
                generateCode(3),
                "투표 설명이 비어있습니다.",
                null,
                Category.VALIDATION
        );
    }

    public static VoteDomainException invalidDescriptionLength(int minLength, int maxLength) {
        return new VoteDomainException(
                generateCode(4),
                "투표 설명의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
