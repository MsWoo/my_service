package mswoo.toyproject.my_service.domain.dto;

import lombok.Builder;
import lombok.Data;
import mswoo.toyproject.my_service.domain.entity.Member;

@Data
@Builder
public class MemberDto {
    private Long id;
    private String userId;
    private String userName;
    private String phoneNumber;

    public static MemberDto toDto(Member entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userName(entity.getUserName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
