package ms.toy.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationRequestDto {
    @NotNull(message = "공간 Seq는 공백일 수 없습니다.")
    @Schema(description = "공간 Seq", example = "1")
    private Integer spaceId;

    @NotBlank(message = "예약일자는 공백일 수 없습니다.")
    @Schema(description = "예약일자", example = "2024-04-23")
    private String reservationDate;

    @NotNull(message = "참석 인원은 공백일 수 없습니다.")
    @Schema(description = "참석 인원", example = "6")
    private Integer attendCount;

    @Schema(description = "예약 메모", example = "ㅇㅇ 회의를 위한 예약 신청")
    private String comment;
}
