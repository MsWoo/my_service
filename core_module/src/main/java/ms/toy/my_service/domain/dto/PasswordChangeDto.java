package ms.toy.my_service.domain.dto;

import lombok.Getter;

@Getter
public class PasswordChangeDto {
    private String password;
    private String newPassword;
}
