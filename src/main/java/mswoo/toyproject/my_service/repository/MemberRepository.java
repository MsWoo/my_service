package mswoo.toyproject.my_service.repository;

import java.util.Optional;
import mswoo.toyproject.my_service.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
