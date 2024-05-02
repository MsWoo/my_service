package ms.toy.my_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ms.toy.my_service.domain.entity.Users;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;
}
