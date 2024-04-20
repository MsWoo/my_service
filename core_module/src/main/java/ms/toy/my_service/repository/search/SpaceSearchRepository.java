package ms.toy.my_service.repository.search;

import ms.toy.my_service.domain.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpaceSearchRepository {
    Page<Space> searchSpace(SpaceSearchCondition searchCondition, Pageable pageable);
}
