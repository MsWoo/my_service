package ms.toy.my_service.mapper;

import ms.toy.my_service.config.MapstructConfig;
import ms.toy.my_service.domain.dto.AdminDetailDto;
import ms.toy.my_service.domain.dto.AdminDto;
import ms.toy.my_service.domain.dto.AdminJoinDto;
import ms.toy.my_service.domain.dto.AdminListDto;
import ms.toy.my_service.domain.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface AdminMapper {

    @Mapping(source = "userId", target = "createdBy")
    Admin toEntity(AdminJoinDto adminJoinDto, String userId);

    AdminDto toDto(Admin entity);

    AdminListDto toListDto(Admin entity);
    AdminDetailDto toDetailDto(Admin entity);

}
