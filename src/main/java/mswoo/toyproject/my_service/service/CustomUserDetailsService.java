package mswoo.toyproject.my_service.service;

import io.micrometer.common.util.StringUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.domain.entity.Authority;
import mswoo.toyproject.my_service.enums.ErrorCode;
import mswoo.toyproject.my_service.config.jwt.CustomDetailsSerivce;
import mswoo.toyproject.my_service.domain.entity.Member;
import mswoo.toyproject.my_service.enums.Role;
import mswoo.toyproject.my_service.repository.AuthorityRepository;
import mswoo.toyproject.my_service.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements CustomDetailsSerivce {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.EMPTY_DATA.name()));

        // 로그인 잠김 유효성 체크
        if (member.getLoginLockTime() != null && member.getLoginLockTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_LOCK.name());
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            member.increaseFailCount();
            memberRepository.save(member);
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_FAIL.name());
        }

        // == 로그인 성공 ==
        member.initLoginFail();
        memberRepository.save(member);

        Authority authority = authorityRepository.findById(member.getAuthorityId()).get();
        Role role = Role.valueOf(authority.getAuthorityName());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));

        User user = new User(
                member.getUserId(),
                "",
                grantedAuthorities
        );

        return user;
    }
}
