package kr.composite.api.vote.application;

import kr.composite.api.exception.domain.BusinessException;

class VoteApplicationException extends BusinessException {

    private static final String CODE_PREFIX = "VOTE_APPLICATION_";

    private VoteApplicationException(String code, String message, Category category) {
        super(code, message, category);
    }

    static VoteApplicationException widgetNotFound() {
        return new VoteApplicationException(
                generateCode(1),
                "투표 위젯을 찾을 수 없습니다.",
                Category.NOT_FOUND
        );
    }

    static VoteApplicationException alreadySubmitted() {
        return new VoteApplicationException(
                generateCode(2),
                "이미 투표를 완료했습니다.",
                Category.CONFLICT
        );
    }

    static VoteApplicationException invalidOptionForVote() {
        return new VoteApplicationException(
                generateCode(3),
                "해당 투표에 속하지 않는 선택지입니다.",
                Category.VALIDATION
        );
    }

    static VoteApplicationException voteNotInProgress() {
        return new VoteApplicationException(
                generateCode(4),
                "진행 중인 투표가 아닙니다.",
                Category.VALIDATION
        );
    }

    static VoteApplicationException emptyOptionIds() {
        return new VoteApplicationException(
                generateCode(5),
                "선택지를 하나 이상 선택해야 합니다.",
                Category.VALIDATION
        );
    }

    static VoteApplicationException multipleOptionsNotAllowed() {
        return new VoteApplicationException(
                generateCode(6),
                "복수 선택이 허용되지 않는 투표입니다.",
                Category.VALIDATION
        );
    }

    static VoteApplicationException emptyOptions() {
        return new VoteApplicationException(
                generateCode(7),
                "투표 선택지를 하나 이상 입력해야 합니다.",
                Category.VALIDATION
        );
    }

    private static String generateCode(int number) {
        return CODE_PREFIX + String.format("%03d", number);
    }
}
