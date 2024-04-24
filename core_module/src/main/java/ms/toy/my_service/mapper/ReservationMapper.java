package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.ReservationDto;
import ms.toy.my_service.domain.dto.ReservationRequestDto;
import ms.toy.my_service.domain.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface ReservationMapper {
    @Mapping(source = "userId", target = "createdBy")
    Reservation toEntity(ReservationRequestDto reservationRequestDto, String userId);

    @Mapping(source = "entity.space.id", target = "spaceId")
    ReservationDto toDto(Reservation entity);
}
