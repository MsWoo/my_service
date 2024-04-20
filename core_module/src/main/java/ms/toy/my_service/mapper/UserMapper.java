package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.*;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.domain.entity.Users;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserMapper {

    UserDto toDto(Users entity);

    UserListDto toListDto(Users entity);
    UserDetailDto toDetailDto(Users entity);

}
