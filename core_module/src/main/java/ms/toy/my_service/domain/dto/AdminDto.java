package ms.toy.my_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import ms.toy.my_service.domain.entity.Admin;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    public static AdminDto toDto(Admin entity) {
//        return AdminDto.builder()
//                .id(entity.getId())
//                .userId(entity.getUserId())
//                .authorityId(entity.getAuthorityId())
//                .userName(entity.getUserName())
//                .phoneNumber(entity.getPhoneNumber())
//                .build();
//    }
}
