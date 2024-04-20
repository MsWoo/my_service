package ms.toy.my_service.service;

import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.*;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.mapper.AdminMapper;
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
    private final AdminMapper adminMapper;

    public CommonPageDto searchAdmin(AdminSearchCondition adminSearchCondition) {
        Page<Admin> adminPage = adminRepository.searchAdmin(adminSearchCondition, adminSearchCondition.getPageable());

        return CommonPageDto.builder()
                .list(adminPage.stream()
                        .map(adminMapper::toListDto)
                        .collect(Collectors.toList()))
                .pageIndex(adminSearchCondition.getPageIndex())
                .pageSize(adminSearchCondition.getPageSize())
                .totalPage(adminPage.getTotalPages())
                .totalCount(adminPage.getTotalElements())
                .build();
    }

    public AdminDetailDto getAdminInfo(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        return adminMapper.toDetailDto(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminDto joinAdmin(AdminJoinDto adminJoinDto) {
        if (adminRepository.existsByUserId(adminJoinDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.DUPLICATE_ID.name());
        }

        Admin admin = adminMapper.toEntity(adminJoinDto);
        admin.setAuthorityId(2L);
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setCreatedBy(adminJoinDto.getUserId());

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

        admin.update(adminEditDto, userInfo.getUserId());

        return AdminDto.builder().id(id).build();
    }
}
