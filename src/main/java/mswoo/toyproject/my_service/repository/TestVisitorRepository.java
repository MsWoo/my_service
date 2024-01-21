package mswoo.toyproject.my_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import mswoo.toyproject.my_service.domain.entity.TestVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestVisitorRepository extends JpaRepository<TestVisitor, Long> {
    List<TestVisitor> findAllByCreatedAtIsBetween(LocalDateTime start, LocalDateTime end);
}
