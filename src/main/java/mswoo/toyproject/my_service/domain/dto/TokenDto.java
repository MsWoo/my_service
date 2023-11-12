package mswoo.toyproject.my_service.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
