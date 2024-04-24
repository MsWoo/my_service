package ms.toy.my_service.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SpaceRequestDto {
    @NotBlank(message = "공간 이름은 공백일 수 없습니다.")
    @Schema(description = "공간 이름", example = "회의실-1")
    private String spaceName;

    @Schema(description = "공간 설명", example = "1번 회의실")
    private String spaceDescription;

    @NotNull(message = "수용 인원은 공백일 수 없습니다.")
    @Schema(description = "수용 인원", example = "6")
    private Integer capacity;

    @NotBlank(message = "공간 사용 여부는 공백일 수 없습니다.")
    @Schema(description = "공간 사용 여부", example = "Y")
    private String useYn;
}
