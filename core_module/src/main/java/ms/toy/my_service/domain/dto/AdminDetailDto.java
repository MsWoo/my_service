package ms.toy.my_service.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdminDetailDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;
}
