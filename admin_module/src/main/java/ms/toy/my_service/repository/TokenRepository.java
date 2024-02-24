package ms.toy.my_service.repository;

import ms.toy.my_service.domain.entity.TokenInfo;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenInfo, String> {
}
