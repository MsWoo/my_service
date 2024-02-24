package ms.toy.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDto {
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Schema(description = "아이디", example = "userId")
    private String userId;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Schema(description = "비밀번호", example = "password12#")
    private String password;
}
