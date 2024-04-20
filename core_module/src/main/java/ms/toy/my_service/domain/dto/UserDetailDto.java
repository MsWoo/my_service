package ms.toy.my_service.domain.dto;

import lombok.Data;

@Data
public class UserDetailDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;
}
