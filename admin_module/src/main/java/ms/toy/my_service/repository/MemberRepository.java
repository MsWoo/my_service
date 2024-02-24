package ms.toy.my_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ms.toy.my_service.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
    List<Member> findAllByCreatedAtIsBetween(LocalDateTime start, LocalDateTime end);
}
