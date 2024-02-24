package ms.toy.my_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.domain.entity.Authority;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.enums.Role;
import ms.toy.my_service.jwt.CustomDetailsSerivce;
import ms.toy.my_service.repository.AdminRepository;
import ms.toy.my_service.repository.AuthorityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements CustomDetailsSerivce {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        Admin admin = adminRepository.findByUserId(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        // 로그인 잠김 유효성 체크
        if (admin.getLoginLockTime() != null && admin.getLoginLockTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_LOCK.name());
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            admin.increaseFailCount();
            adminRepository.save(admin);
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_FAIL.name());
        }

        // == 로그인 성공 ==
        admin.initLoginFail();
        adminRepository.save(admin);

        Authority authority = authorityRepository.findById(admin.getAuthorityId()).get();
        Role role = Role.valueOf(authority.getAuthorityName());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));

        User user = new User(
                admin.getUserId(),
                "",
                grantedAuthorities
        );

        return user;
    }
}
