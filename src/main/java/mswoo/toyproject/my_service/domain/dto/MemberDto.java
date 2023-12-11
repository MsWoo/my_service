package mswoo.toyproject.my_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import mswoo.toyproject.my_service.domain.entity.Member;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {
    private Long id;
    private String userId;
    private Long authorityId;
    private String userName;
    private String phoneNumber;

    public static MemberDto toDto(Member entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .authorityId(entity.getAuthorityId())
                .userName(entity.getUserName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
