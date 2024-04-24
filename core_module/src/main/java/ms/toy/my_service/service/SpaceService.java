package ms.toy.my_service.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.SpaceDto;
import ms.toy.my_service.domain.dto.SpaceRequestDto;
import ms.toy.my_service.domain.entity.Space;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.mapper.SpaceMapper;
import ms.toy.my_service.repository.SpaceRepository;
import ms.toy.my_service.repository.search.SpaceSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final SpaceMapper spaceMapper;


    public CommonPageDto searchSpace(SpaceSearchCondition searchCondition) {
        Page<Space> spacePage = spaceRepository.searchSpace(searchCondition, searchCondition.getPageable());

        return CommonPageDto.builder()
                .list(spacePage.stream()
                        .map(spaceMapper::toDto)
                        .collect(Collectors.toList()))
                .pageIndex(searchCondition.getPageIndex())
                .pageSize(searchCondition.getPageSize())
                .totalPage(spacePage.getTotalPages())
                .totalCount(spacePage.getTotalElements())
                .build();
    }

    public SpaceDto getSpaceInfo(Long id) {
        Space space = spaceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return spaceMapper.toDto(space);
    }

    @Transactional(rollbackFor = Exception.class)
    public SpaceDto saveSpace(SpaceRequestDto spaceRequestDto, MemberInfo memberInfo) {
        if (spaceRepository.existsBySpaceName(spaceRequestDto.getSpaceName())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_SPACE_NAME.name());
        }

        Space space = spaceMapper.toEntity(spaceRequestDto, memberInfo.getUsername());

        Long id = spaceRepository.save(space).getId();

        return SpaceDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public SpaceDto deleteSpace(Long id) {
        spaceRepository.deleteById(id);
        return SpaceDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public SpaceDto editSpace(Long id, SpaceRequestDto spaceRequestDto, MemberInfo memberInfo) {
        Space space = spaceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        space.update(spaceRequestDto, memberInfo.getUsername());
        return SpaceDto.builder().id(id).build();
    }
}
