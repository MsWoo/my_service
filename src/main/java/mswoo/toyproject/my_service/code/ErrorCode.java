package mswoo.toyproject.my_service.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SYSTEM_ERROR("0000", "시스템 에러입니다."),
    EMPTY_DATA("0001", "데이터가 없습니다."),

    LOGIN_FAIL("1000", "ID 혹은 비밀번호가 일치하지 않습니다. 입력한 내용을 다시 확인해 주세요."),
    LOGIN_LOCK("1001", "로그인 5회 실패 시 5분동안 로그인이 제한됩니다."),

    DUPLICATE_ID("2000", "중복된 ID가 있습니다."),
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
