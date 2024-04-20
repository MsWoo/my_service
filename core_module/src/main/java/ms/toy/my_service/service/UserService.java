package ms.toy.my_service.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.CommonPageDto;
import ms.toy.my_service.domain.dto.UserDto;
import ms.toy.my_service.domain.entity.Users;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.repository.UserRepository;
import ms.toy.my_service.repository.search.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CommonPageDto searchUser(UserSearchCondition searchCondition) {
        Page<Users> userPage = userRepository.searchUser(searchCondition, searchCondition.getPageable());

        return CommonPageDto.builder()
                .list(userPage.stream()
                        .map(UserDto::toDto)
                        .collect(Collectors.toList()))
                .pageIndex(searchCondition.getPageIndex())
                .pageSize(searchCondition.getPageSize())
                .totalPage(userPage.getTotalPages())
                .totalCount(userPage.getTotalElements())
                .build();
    }

    public UserDto getUserInfo(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return UserDto.toDto(user);
    }
}
