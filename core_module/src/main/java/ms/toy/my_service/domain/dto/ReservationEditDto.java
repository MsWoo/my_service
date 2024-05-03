package ms.toy.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReservationEditDto {
    @Schema(description = "예약 시작 일자", example = "yyyy-MM-dd HH:mm:ss")
    private String reservationStartDt;

    @Schema(description = "예약 종료 일자", example = "yyyy-MM-dd HH:mm:ss")
    private String reservationEndDt;

    @Schema(description = "참석 인원", example = "6")
    private Integer attendCount;

    @Schema(description = "예약 메모", example = "ㅇㅇ 회의를 위한 예약 신청")
    private String comment;
}
