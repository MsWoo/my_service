package ms.toy.my_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ms.toy.my_service.domain.entity.Admin;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;

    public static AdminDto toDto(Admin entity) {
        return AdminDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .authorityId(entity.getAuthorityId())
                .userName(entity.getUserName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
