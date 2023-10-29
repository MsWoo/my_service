package mswoo.toyproject.my_service.service;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.config.jwt.TokenProvider;
import mswoo.toyproject.my_service.domain.dto.LoginDto;
import mswoo.toyproject.my_service.domain.dto.TokenDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenInfo = tokenProvider.generateToken(authentication);

        return tokenInfo;
    }
}
