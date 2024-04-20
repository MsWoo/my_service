package ms.toy.my_service.domain.dto;

import lombok.Data;

@Data
public class UserListDto {
    private Long id;
    private String userId;
    private String userName;
}
