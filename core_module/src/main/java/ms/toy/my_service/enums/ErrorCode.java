package ms.toy.my_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 0000
    SYSTEM_ERROR("0000", "시스템 에러입니다."),
    EMPTY_DATA("0001", "데이터가 없습니다."),

    // 공통 1000
    LOGIN_FAIL("1000", "ID 혹은 비밀번호가 일치하지 않습니다. 입력한 내용을 다시 확인해 주세요."),
    LOGIN_LOCK("1001", "로그인 5회 실패 시 5분동안 로그인이 제한됩니다."),
    DUPLICATE_ID("1003", "이미 등록된 ID입니다."),
    DELETED_ID("1004", "이미 삭제된 ID입니다."),
    WRONG_PASSWORD("1005", "패스워드가 일치하지 않습니다."),


    // 관리자 Admin 2000
//    DUPLICATE_ID("2000", "이미 등록된 ID입니다."),

    // 사용자 Users 3000
//    DUPLICATE_ID("3000", "이미 등록된 ID입니다."),

    // 공간 Space 4000
    DUPLICATE_SPACE_NAME("4000", "이미 등록된 공간명입니다."),
    UNUSABLE_SPACE("4001", "사용 불가능한 공간입니다."),
    DELETED_SPACE("4002", "삭제된 공간입니다."),

    // 예약 Reservation 5000
    NOT_VALID_DATE("5000", "유효하지 않은 날짜입니다."),
    ALREADY_RESERVED_SPACE("5001", "해당 날짜에 이미 예약 된 공간입니다."),
    ;

    private String errorCode;
    private String errorMessage;

    public static ErrorCode getErrorCode(ResponseStatusException responseStatusException) {
        if (!StringUtils.hasText(responseStatusException.getReason())) {
            return ErrorCode.SYSTEM_ERROR;
        }
        try {
            return ErrorCode.valueOf(responseStatusException.getReason());
        } catch (IllegalArgumentException e) {
            return ErrorCode.SYSTEM_ERROR;
        }
    }
}
