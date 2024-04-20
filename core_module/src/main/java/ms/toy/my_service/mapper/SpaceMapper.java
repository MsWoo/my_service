package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.SpaceDto;
import ms.toy.my_service.domain.dto.SpaceRequestDto;
import ms.toy.my_service.domain.entity.Space;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface SpaceMapper {
    Space toEntity(SpaceRequestDto spaceRequestDto);
    SpaceDto toDto(Space entity);
}
