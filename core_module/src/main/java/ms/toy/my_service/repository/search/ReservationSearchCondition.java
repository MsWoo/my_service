package ms.toy.my_service.repository.search;

import java.time.LocalDate;
import lombok.Data;
import ms.toy.my_service.enums.ReservationStatus;

@Data
public class ReservationSearchCondition extends SearchCondition {
    private Long userId;
    private Long spaceId;
    private ReservationStatus status;
    private LocalDate reservationDate;
}
