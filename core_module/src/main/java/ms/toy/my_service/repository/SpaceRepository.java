package ms.toy.my_service.repository;

import ms.toy.my_service.domain.entity.Space;
import ms.toy.my_service.repository.search.SpaceSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceSearchRepository {
    boolean existsBySpaceName(String spaceName);
}
