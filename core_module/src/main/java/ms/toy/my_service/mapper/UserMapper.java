package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.UserDetailDto;
import ms.toy.my_service.domain.dto.UserDto;
import ms.toy.my_service.domain.dto.UserJoinDto;
import ms.toy.my_service.domain.dto.UserListDto;
import ms.toy.my_service.domain.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserMapper {
    Users toEntity(UserJoinDto userJoinDto);

    UserDto toDto(Users entity);

    UserListDto toListDto(Users entity);
    UserDetailDto toDetailDto(Users entity);

}
