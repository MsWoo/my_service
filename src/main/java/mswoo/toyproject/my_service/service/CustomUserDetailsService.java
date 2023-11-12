package mswoo.toyproject.my_service.service;

import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.code.ErrorCode;
import mswoo.toyproject.my_service.config.jwt.CustomDetailsSerivce;
import mswoo.toyproject.my_service.domain.entity.Member;
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

    @Override
    public UserDetails loadUserByUsername(String username, String password) {

        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("ID를 입력해주세요.");
        }
        if (StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("Password를 입력해주세요.");
        }

        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 Member입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.LOGIN_FAIL.name());
        }

        // 임시로 USER 권한 부여
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        User user = new User(
                member.getUserId(),
                "",
                grantedAuthorities
        );

        return user;
    }
}
