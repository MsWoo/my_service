package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.ReservationDto;
import ms.toy.my_service.domain.dto.ReservationRequestDto;
import ms.toy.my_service.domain.entity.Reservation;
import ms.toy.my_service.domain.entity.Space;
import ms.toy.my_service.jwt.MemberInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface ReservationMapper {
    @Mapping(source = "space", target = "space")
    @Mapping(source = "memberInfo.memberSeq", target = "userId")
    @Mapping(source = "memberInfo.username", target = "createdBy")
    Reservation toEntity(ReservationRequestDto reservationRequestDto, Space space, MemberInfo memberInfo);

    @Mapping(source = "entity.space.id", target = "spaceId")
    ReservationDto toDto(Reservation entity);
}
