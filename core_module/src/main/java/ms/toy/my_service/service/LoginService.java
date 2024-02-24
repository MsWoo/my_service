package ms.toy.my_service.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ms.toy.my_service.domain.dto.LoginDto;
import ms.toy.my_service.domain.dto.TokenDto;
import ms.toy.my_service.domain.entity.TokenInfo;
import ms.toy.my_service.jwt.TokenProvider;
import ms.toy.my_service.util.CookieUtil;
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
    private final TokenInfoService tokenInfoService;

    public TokenDto login(LoginDto loginDto, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        CookieUtil.generateCookie(response, "accessToken", tokenDto.getAccessToken());

        tokenInfoService.saveTokenInfo(
                TokenInfo.builder()
                        .accessToken(tokenDto.getAccessToken())
                        .refreshToken(tokenDto.getRefreshToken())
                        .build()
        );

        return tokenDto;
    }
}
