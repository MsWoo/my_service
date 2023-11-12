package mswoo.toyproject.my_service.domain.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private boolean result;
    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.setResult(false);
        this.setCode(code);
        this.setMessage(message);
    }
}
