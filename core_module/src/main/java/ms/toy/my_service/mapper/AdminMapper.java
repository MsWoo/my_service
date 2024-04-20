package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.AdminDetailDto;
import ms.toy.my_service.domain.dto.AdminDto;
import ms.toy.my_service.domain.dto.AdminJoinDto;
import ms.toy.my_service.domain.dto.AdminListDto;
import ms.toy.my_service.domain.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface AdminMapper {

    Admin toEntity(AdminJoinDto adminJoinDto);

    AdminDto toDto(Admin entity);

    AdminListDto toListDto(Admin entity);
    AdminDetailDto toDetailDto(Admin entity);

}
