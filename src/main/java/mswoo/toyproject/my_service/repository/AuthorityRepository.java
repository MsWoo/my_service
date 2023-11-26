package mswoo.toyproject.my_service.repository;

import mswoo.toyproject.my_service.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
