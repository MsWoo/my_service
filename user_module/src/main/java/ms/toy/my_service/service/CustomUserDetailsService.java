package ms.toy.my_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.entity.Authority;
import ms.toy.my_service.domain.entity.Users;
import ms.toy.my_service.enums.ErrorCode;
import ms.toy.my_service.enums.Role;
import ms.toy.my_service.jwt.CustomDetailsSerivce;
import ms.toy.my_service.repository.AuthorityRepository;
import ms.toy.my_service.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        Users users = userRepository.findByUserId(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        // 로그인 잠김 유효성 체크
        if (users.getLoginLockTime() != null && users.getLoginLockTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_LOCK.name());
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, users.getPassword())) {
            users.increaseFailCount();
            userRepository.save(users);
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_FAIL.name());
        }

        // == 로그인 성공 ==
        users.initLoginFail();
        userRepository.save(users);

        Authority authority = authorityRepository.findById(users.getAuthorityId()).get();
        Role role = Role.valueOf(authority.getAuthorityName());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));

        User user = new User(
                users.getUserId(),
                "",
                grantedAuthorities
        );

        return user;
    }
}
