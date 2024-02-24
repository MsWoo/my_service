package ms.toy.my_service.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.AdminDto;
import ms.toy.my_service.domain.dto.AdminEditDto;
import ms.toy.my_service.domain.dto.AdminJoinDto;
import ms.toy.my_service.domain.dto.UserInfo;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public List<AdminDto> searchAdmin() {
        return adminRepository.findAll().stream()
                .map(AdminDto::toDto)
                .collect(Collectors.toList());
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
                .authorityId(1L)
                .userId(adminJoinDto.getUserId())
                .userName(adminJoinDto.getUserName())
                .password(passwordEncoder.encode(adminJoinDto.getPassword()))
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
