package ms.toy.my_service.service;

import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.*;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.repository.AdminRepository;
import ms.toy.my_service.repository.search.AdminSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public CommonPageDto searchAdmin(AdminSearchCondition adminSearchCondition) {
        Page<Admin> adminPage = adminRepository.searchAdmin(adminSearchCondition, adminSearchCondition.getPageable());

        return CommonPageDto.builder()
                .list(adminPage.stream()
                        .map(AdminDto::toDto)
                        .collect(Collectors.toList()))
                .pageIndex(adminSearchCondition.getPageIndex())
                .pageSize(adminSearchCondition.getPageSize())
                .totalPage(adminPage.getTotalPages())
                .totalCount(adminPage.getTotalElements())
                .build();
    }

    public AdminDto getAdminInfo(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return AdminDto.toDto(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminDto joinAdmin(AdminJoinDto adminJoinDto) {
        if (adminRepository.existsByUserId(adminJoinDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_ID.name());
        }

        // todo [gotoend] mapper로 전환 필요
        // 필드가 추가될때마다 변경해줘야한다. mapper로 한방에 변환
        Admin admin = Admin.builder()
                .authorityId(2L)
                .userId(adminJoinDto.getUserId())
                .userName(adminJoinDto.getUserName())
                .password(passwordEncoder.encode("1234"))
                .phoneNumber(adminJoinDto.getPhoneNumber())
                .createdBy(adminJoinDto.getUserId())
                .build();

        Long id = adminRepository.save(admin).getId();

        return AdminDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminDto deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        return AdminDto.builder().id(id).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminDto editAdmin(Long id, AdminEditDto adminEditDto, UserInfo userInfo) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        // todo [gotoend] mapper로 전환 필요
        admin.update(adminEditDto, userInfo.getUserId());

        return AdminDto.builder().id(id).build();
    }
}
