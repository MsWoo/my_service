package ms.toy.my_service.domain.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdminListDto {
    private Long id;
    private String userId;
    private String userName;
}
