package ms.toy.my_service.repository.search;

import ms.toy.my_service.domain.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationSearchRepository {
    Page<Reservation> searchReservation(ReservationSearchCondition searchCondition, Pageable pageable);
}
