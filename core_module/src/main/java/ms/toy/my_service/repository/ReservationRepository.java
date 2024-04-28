package ms.toy.my_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import ms.toy.my_service.domain.entity.Reservation;
import ms.toy.my_service.repository.search.ReservationSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationSearchRepository {
    @Query("SELECT r " +
            "FROM Reservation r " +
            "WHERE r.space.id = :spaceId and ((r.reservationStartDt = :startDt and r.reservationEndDt = :endDt) or (r.reservationStartDt > :startDt and r.reservationEndDt < :endDt) or (r.reservationStartDt <= :startDt and r.reservationEndDt > :startDt) or (r.reservationStartDt < :endDt and r.reservationEndDt >= :endDt))")
    List<Reservation> findBySpaceIdAndStartDtAndEndDtBetween(@Param("spaceId") Integer spaceId, @Param("startDt") LocalDateTime startDt, @Param("endDt") LocalDateTime endDt);
}
