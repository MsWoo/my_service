package ms.toy.my_service.repository.search;

import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSearchRepository {
    Page<Admin> searchAdmin(AdminSearchCondition searchCondition, Pageable pageable);
}
