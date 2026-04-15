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

    static VoteDomainException emptyTitle() {
        return new VoteDomainException(
                generateCode(1),
                "투표 제목이 비어있습니다.",
                Category.VALIDATION
        );
    }

    static VoteDomainException invalidTitleLength(String title, int minLength, int maxLength) {
        return new VoteDomainException(
                generateCode(2),
                "투표 제목의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 제목: " + title + " (길이: " + title.length() + ")",
                Category.VALIDATION
        );
    }

    static VoteDomainException unsupportedStatus(String description) {
        return new VoteDomainException(
                generateCode(3),
                "지원하지 않는 투표 상태입니다.",
                "제공된 상태: " + description,
                Category.VALIDATION
        );
    }

    static VoteDomainException invalidStatusTransition(VoteStatus current, VoteStatus target) {
        return new VoteDomainException(
                generateCode(4),
                "투표 상태를 변경할 수 없습니다.",
                "현재 상태: " + current.getDescription() + ", 변경 요청 상태: " + target.getDescription(),
                Category.VALIDATION
        );
    }

    static VoteDomainException emptyOptionContent() {
        return new VoteDomainException(
                generateCode(5),
                "투표 선택지 내용이 비어있습니다.",
                Category.VALIDATION
        );
    }

    static VoteDomainException invalidOptionContentLength(String content, int minLength, int maxLength) {
        return new VoteDomainException(
                generateCode(6),
                "투표 선택지 내용의 길이가 유효하지 않습니다. (최소 " + minLength + "자, 최대 " + maxLength + "자)",
                "제공된 내용 길이: " + content.length(),
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
