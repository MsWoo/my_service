package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.SpaceDto;
import ms.toy.my_service.domain.dto.SpaceRequestDto;
import ms.toy.my_service.domain.entity.Space;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface SpaceMapper {
    @Mapping(source = "userId", target = "createdBy")
    Space toEntity(SpaceRequestDto spaceRequestDto, String userId);
    SpaceDto toDto(Space entity);
}
