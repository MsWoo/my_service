package mswoo.toyproject.my_service.domain.dto;

import lombok.Getter;

@Getter
public class MemberJoinDto {
    private String userId;
    private String userName;
    private String password;
    private String phoneNumber;
}
