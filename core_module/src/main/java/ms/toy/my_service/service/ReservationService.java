package ms.toy.my_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.toy.my_service.aop.DistributedLock;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.ReservationDto;
import ms.toy.my_service.domain.dto.ReservationEditDto;
import ms.toy.my_service.domain.dto.ReservationRequestDto;
import ms.toy.my_service.domain.entity.Reservation;
import ms.toy.my_service.domain.entity.Space;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.enums.ReservationStatus;
import ms.toy.my_service.enums.SiteType;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.mapper.ReservationMapper;
import ms.toy.my_service.repository.ReservationRepository;
import ms.toy.my_service.repository.SpaceRepository;
import ms.toy.my_service.repository.search.ReservationSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SpaceRepository spaceRepository;
    private final ReservationMapper reservationMapper;

    public CommonPageDto searchReservation(ReservationSearchCondition searchCondition, SiteType siteType, MemberInfo memberInfo) {
        if (SiteType.USER.equals(siteType)) {
            searchCondition.setUserId(Long.valueOf(memberInfo.getMemberSeq()));
            searchCondition.setAdminYn("N");
        }

        Page<Reservation> reservationPage = reservationRepository.searchReservation(searchCondition, searchCondition.getPageable());

        return CommonPageDto.builder()
                .list(reservationPage.stream()
                        .map(reservationMapper::toDto)
                        .collect(Collectors.toList()))
                .pageIndex(searchCondition.getPageIndex())
                .pageSize(searchCondition.getPageSize())
                .totalPage(reservationPage.getTotalPages())
                .totalCount(reservationPage.getTotalElements())
                .build();
    }

    public ReservationDto getReservationInfo(Long id, SiteType siteType, MemberInfo memberInfo) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        if (SiteType.USER.equals(siteType)) {
            if ("N".equals(reservation.getAdminYn()) && !reservation.getUserId().equals(Long.valueOf(memberInfo.getMemberSeq()))) {
                throw new ResponseStatusException(HttpStatus.OK, ErrorCode.USER_MISMATCH.name());
            }
        }

        return reservationMapper.toDto(reservation);
    }

    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "#reservationRequestDto.getReservationDate().concat('-').concat(#reservationRequestDto.getSpaceId())")
    public ReservationDto saveReservation(ReservationRequestDto reservationRequestDto, SiteType siteType, MemberInfo memberInfo) {
        // 날짜 유효성 검증
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDt = LocalDateTime.parse(reservationRequestDto.getReservationStartDt(), formatter);
        LocalDateTime endDt = LocalDateTime.parse(reservationRequestDto.getReservationEndDt(), formatter);
        if (startDt.isBefore(now) || startDt.isAfter(endDt)) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.NOT_VALID_DATE.name());
        }

        // 공간 사용 및 삭제 여부 유효성 검증
        Space space = spaceRepository.findById(Long.valueOf(reservationRequestDto.getSpaceId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,ErrorCode.EMPTY_DATA.name()));
        if ("N".equals(space.getUseYn())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.UNUSABLE_SPACE.name());
        }
        if ("Y".equals(space.getDeleteYn())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DELETED_SPACE.name());
        }

        // 공간 예약 가능 여부 유효성 검증
        List<Reservation> checkReservationList = reservationRepository.findBySpaceIdAndStartDtAndEndDtBetween(reservationRequestDto.getSpaceId(), startDt, endDt);
        if (!ObjectUtils.isEmpty(checkReservationList)) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.ALREADY_RESERVED_SPACE.name());
        }

        // 예약 엔티티 변환 및 초기 값 설정
        Reservation reservation = reservationMapper.toEntity(reservationRequestDto, startDt, endDt, space, memberInfo);
        reservation.setStatus(ReservationStatus.ACCEPTED);
        reservation.setAdminYn((SiteType.ADMIN.equals(siteType)) ? "Y" : "N");

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDto(savedReservation);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReservationDto cancelReservation(Long id, SiteType siteType, MemberInfo memberInfo) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        if (SiteType.USER.equals(siteType)) {
            if ("N".equals(reservation.getAdminYn()) && !reservation.getUserId().equals(Long.valueOf(memberInfo.getMemberSeq()))) {
                throw new ResponseStatusException(HttpStatus.OK, ErrorCode.USER_MISMATCH.name());
            }
        }

        reservation.cancel(memberInfo.getUsername());

        return reservationMapper.toDto(reservation);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReservationDto editReservation(Long id, ReservationEditDto reservationEditDto, SiteType siteType, MemberInfo memberInfo) {
        // todo 유효성 검증 필요 (날짜, 예약 가능 여부, 취소 상태, )
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        if (SiteType.USER.equals(siteType)) {
            if ("N".equals(reservation.getAdminYn()) && !reservation.getUserId().equals(Long.valueOf(memberInfo.getMemberSeq()))) {
                throw new ResponseStatusException(HttpStatus.OK, ErrorCode.USER_MISMATCH.name());
            }
        }

        reservation.update(reservationEditDto, memberInfo.getUsername());

        return reservationMapper.toDto(reservation);
    }
}
