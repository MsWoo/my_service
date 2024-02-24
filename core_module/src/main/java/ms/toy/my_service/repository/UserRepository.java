package ms.toy.my_service.repository;

import java.util.Optional;
import ms.toy.my_service.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
