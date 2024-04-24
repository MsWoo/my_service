package ms.toy.my_service.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import ms.toy.my_service.enums.ReservationStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long spaceId;
    private Integer attendCount;
    private ReservationStatus status;
    private LocalDate reservationDate;
    private String comment;
    private String adminYn;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
