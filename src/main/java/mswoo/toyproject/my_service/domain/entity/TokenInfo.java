package mswoo.toyproject.my_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "tokenInfo", timeToLive = 60*60*3)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    @Id
    private String accessToken;
    private String refreshToken;
}
