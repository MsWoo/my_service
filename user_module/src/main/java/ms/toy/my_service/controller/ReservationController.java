package ms.toy.my_service.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Object> searchReservation(
            @ParameterObject @ModelAttribute ReservationSearchCondition searchCondition,
            @AuthenticationPrincipal MemberInfo memberInfo
    ) {
        return ResponseEntity.ok(reservationService.searchReservation(searchCondition, SiteType.USER, memberInfo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReservationInfo(
            @Parameter(description = "예약 ID") @PathVariable Long id,
            @AuthenticationPrincipal MemberInfo memberInfo
    ) {
        return ResponseEntity.ok(reservationService.getReservationInfo(id, SiteType.USER, memberInfo));
    }

    @PostMapping
    public ResponseEntity<Object> saveReservation(
            @Parameter @Valid @RequestBody ReservationRequestDto reservationRequestDto,
            @AuthenticationPrincipal MemberInfo memberInfo
    ) {
        return ResponseEntity.ok(reservationService.saveReservation(reservationRequestDto, SiteType.USER, memberInfo));
    }


}
