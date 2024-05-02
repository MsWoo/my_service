package ms.toy.my_service.service;

import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.*;
import ms.toy.my_service.domain.entity.Users;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.enums.Role;
import ms.toy.my_service.jwt.MemberInfo;
import ms.toy.my_service.mapper.UserMapper;
import ms.toy.my_service.repository.UserRepository;
import ms.toy.my_service.repository.search.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * (관리자) 이용자 검색
     * @param searchCondition
     * @return
     */
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

    /**
     * (관리자/이용자) 이용자 상세 정보 확인
     * @param id
     * @return
     */
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

    /**
     * 이용자 정보 수정
     * @param id
     * @param userEditDto
     * @param memberInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserDto editUser(Long id, UserEditDto userEditDto, MemberInfo memberInfo) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        user.update(userEditDto, memberInfo.getUsername());

        return UserDto.builder().id(id).build();
    }

    /**
     * 이용자 회원가입
     * @param userJoinDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserDto signUp(UserJoinDto userJoinDto) {
        if (userRepository.existsByUserId(userJoinDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_ID.name());
        }

        Users user = userMapper.toEntity(userJoinDto);
        user.setAuthorityId(Role.USER.getAuthorityId());
        user.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
        user.setCreatedBy(user.getUserId());

        Long id = userRepository.save(user).getId();

        return UserDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDto changePassword(PasswordChangeDto passwordChangeDto, MemberInfo memberInfo) {
        Users user = userRepository.findById(Long.valueOf(memberInfo.getMemberSeq()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        if (!passwordEncoder.matches(passwordChangeDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.WRONG_PASSWORD.name());
        }

        user.changePassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()), memberInfo.getUsername());

        return UserDto.builder().id(user.getId()).build();
    }
}
