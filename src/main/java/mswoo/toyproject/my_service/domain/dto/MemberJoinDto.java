package mswoo.toyproject.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberJoinDto {
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Schema(description = "아이디", example = "userId")
    private String userId;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Schema(description = "이름", example = "홍길동")
    private String userName;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Schema(description = "비밀번호", example = "password12#")
    private String password;

    @NotBlank(message = "휴대폰번호는 공백일 수 없습니다.")
    @Schema(description = "휴대폰번호", example = "010-1234-5678")
    private String phoneNumber;
}
