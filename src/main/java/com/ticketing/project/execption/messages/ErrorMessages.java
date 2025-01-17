package com.ticketing.project.execption.messages;

public class ErrorMessages {
    public static final String UNEXPECTED_ERROR = "예기치 못한 오류가 발생했습니다.";

    public static final String USER_ALREADY_EXIST = "이미 존재하는 사용자입니다.";
    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String USER_NOT_ACTIVE = "회원 탈퇴한 사용자입니다.";

    public static final String INVALID_CREDENTIAL = "아이디 또는 비밀번호가 틀렸습니다.";

    public static final String INVALID_TOKEN = "토큰이 유효하지 않습니다.";

    public static final String LOCATION_NOT_FOUND = "존재하지 않는 공연장입니다.";

    public static final String CONCERT_NOT_FOUND = "존재하지 않는 콘서트입니다.";

    public static final String NO_AVAILABLE_SEAT = "남은 좌석이 없습니다.";

    public static final String RESERVATION_NOT_FOUND = "예약 건을 찾을 수 없습니다.";

    public static final String INVALID_OWNER = "등록자 본인이 아닙니다.";

    public static final String SINGLE_TICKET_PER_USER = "콘서트 당 하나의 티켓만 발급 가능합니다.";
    public static final String ALREADY_CANCEL = "이미 취소된 콘서트입니다.";
}
