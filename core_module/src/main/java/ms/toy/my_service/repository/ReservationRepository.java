package ms.toy.my_service.repository;

import java.time.LocalDate;
import ms.toy.my_service.domain.entity.Reservation;
import ms.toy.my_service.repository.search.ReservationSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationSearchRepository {
    Reservation findBySpaceIdAndReservationDate(Integer spaceId, LocalDate reservationDate);
}
