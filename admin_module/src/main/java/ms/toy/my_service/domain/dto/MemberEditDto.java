package ms.toy.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberEditDto {
    @Schema(description = "이름", example = "홍길동")
    private String userName;

    @Schema(description = "휴대폰번호", example = "010-1234-5678")
    private String phoneNumber;
}
