package ms.toy.my_service.repository;

import ms.toy.my_service.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
