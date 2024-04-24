package ms.toy.my_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.ErrorResponse;
import ms.toy.my_service.domain.dto.ReservationDto;
import ms.toy.my_service.domain.dto.ReservationRequestDto;
import ms.toy.my_service.enums.SiteType;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.repository.search.ReservationSearchCondition;
import ms.toy.my_service.service.ReservationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reservation", description = "예약 관리 Rest API")
@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;


    @Operation(summary = "예약 검색", description = "예약을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = CommonPageDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Object> searchReservation(@ParameterObject @ModelAttribute ReservationSearchCondition searchCondition) {
        return ResponseEntity.ok(reservationService.searchReservation(searchCondition));
    }

    @Operation(summary = "예약 상세 조회", description = "예약을 상세 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getReservationInfo(@Parameter(description = "예약 ID") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationInfo(id));
    }

    @Operation(summary = "예약 등록", description = "새로운 예약을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/save")
    public ResponseEntity<Object> saveReservation(
            @Parameter @Valid @RequestBody ReservationRequestDto reservationRequestDto,
            @AuthenticationPrincipal MemberInfo memberInfo
    ) {
        return ResponseEntity.ok(reservationService.saveReservation(reservationRequestDto, SiteType.ADMIN, memberInfo));
    }

//    @Operation(summary = "예약 취소", description = "기존 예약을 취소합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
//            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> cancelReservation(@Parameter(description = "예약 ID") @PathVariable Long id) {
//        return ResponseEntity.ok(reservationService.cancelReservation(id));
//    }
//
//    @Operation(summary = "예약 수정", description = "기존 예약 정보를 수정합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
//            @ApiResponse(responseCode = "500", description = "FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> editReservation(
//            @Parameter(description = "예약 ID") @PathVariable Long id,
//            @Parameter @RequestBody ReservationRequestDto reservationRequestDto,
//            @AuthenticationPrincipal MemberInfo memberInfo) {
//        return ResponseEntity.ok(reservationService.editReservation(id, reservationRequestDto, memberInfo));
//    }

}
