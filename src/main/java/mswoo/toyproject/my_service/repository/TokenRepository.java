package mswoo.toyproject.my_service.repository;

import java.util.Optional;
import mswoo.toyproject.my_service.domain.entity.TokenInfo;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenInfo, String> {
    Optional<TokenInfo> findByAccessToken(String accessToken);
}
