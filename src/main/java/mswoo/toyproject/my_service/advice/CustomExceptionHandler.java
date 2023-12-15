package mswoo.toyproject.my_service.advice;

import lombok.extern.slf4j.Slf4j;
import mswoo.toyproject.my_service.domain.dto.ErrorResponse;
import mswoo.toyproject.my_service.enums.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 404 Not Found Error Handling
     * @param ex the exception to handle
     * @param headers the headers to use for the response
     * @param status the status code to use for the response
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(ex.getStatusCode()), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * 405 Method Not Allowed Error Handling
     * @param ex the exception to handle
     * @param headers the headers to use for the response
     * @param status the status code to use for the response
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        log.info(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(ex.getStatusCode()), ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * @Valid 적용한 RequestBody 유효성 처리
     * @param ex the exception to handle
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new ErrorResponse(String.valueOf(ex.getStatusCode().value()), errorMessage), ex.getStatusCode());
    }

    /**
     * ResponseStatusException 처리
     * @param e
     * @return
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusException(ResponseStatusException e) {
        ErrorCode errorCode = ErrorCode.getErrorCode(e);
        return new ResponseEntity<>(new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage()), e.getStatusCode());
    }
}
