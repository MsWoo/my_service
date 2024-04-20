package ms.toy.my_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.repository.search.AdminSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminSearchRepository {
    Optional<Admin> findByUserId(String userId);
    boolean existsByUserId(String userId);
    List<Admin> findAllByCreatedAtIsBetween(LocalDateTime start, LocalDateTime end);
}
