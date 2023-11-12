package mswoo.toyproject.my_service.advice;

import mswoo.toyproject.my_service.code.ErrorCode;
import mswoo.toyproject.my_service.domain.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // todo [gotoend] 400, 405, 404 처리

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusException(ResponseStatusException e) {
        ErrorCode errorCode = ErrorCode.getErrorCode(e);
        return new ResponseEntity<>(new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage()), e.getStatusCode());
    }
}
