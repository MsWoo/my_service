package mswoo.toyproject.my_service.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse<T> {
    private boolean result;
    private String code;
    private String message;
    private T data;
}
