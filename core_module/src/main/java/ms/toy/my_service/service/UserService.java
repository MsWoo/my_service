package ms.toy.my_service.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.UserDetailDto;
import ms.toy.my_service.domain.dto.UserDto;
import ms.toy.my_service.domain.dto.UserEditDto;
import ms.toy.my_service.domain.entity.Users;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.mapper.UserMapper;
import ms.toy.my_service.repository.UserRepository;
import ms.toy.my_service.repository.search.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CommonPageDto searchUser(UserSearchCondition searchCondition) {
        Page<Users> userPage = userRepository.searchUser(searchCondition, searchCondition.getPageable());

        return CommonPageDto.builder()
                .list(userPage.stream()
                        .map(userMapper::toListDto)
                        .collect(Collectors.toList()))
                .pageIndex(searchCondition.getPageIndex())
                .pageSize(searchCondition.getPageSize())
                .totalPage(userPage.getTotalPages())
                .totalCount(userPage.getTotalElements())
                .build();
    }

    public UserDetailDto getUserInfo(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return userMapper.toDetailDto(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto deleteUser(Long id, MemberInfo memberInfo) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        user.delete(memberInfo.getUsername());

        return UserDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto editUser(Long id, UserEditDto userEditDto, MemberInfo memberInfo) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        user.update(userEditDto, memberInfo.getUsername());

        return UserDto.builder().id(id).build();
    }
}
