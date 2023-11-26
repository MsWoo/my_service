package mswoo.toyproject.my_service.domain.dto;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private boolean result;
    private String code;
    private String message;
    private T data;

    public SuccessResponse(T data) {
        this.result = true;
        this.code = String.valueOf(HttpServletResponse.SC_OK);
        this.message = "Success";
        this.data = data;
    }
}
