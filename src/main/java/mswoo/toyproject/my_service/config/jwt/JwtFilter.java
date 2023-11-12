package mswoo.toyproject.my_service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mswoo.toyproject.my_service.domain.dto.TokenDto;
import mswoo.toyproject.my_service.service.TokenInfoService;
import mswoo.toyproject.my_service.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final TokenInfoService tokenInfoService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 쿠키 or 헤더에서 토큰 추출
        String accessToken = CookieUtil.getAccessToken((HttpServletRequest) request);
        if (ObjectUtils.isEmpty(accessToken)) {
            accessToken = tokenProvider.resolveToken((HttpServletRequest) request);
        }

        // todo [gotoend] refresh로 갱신해주는 로직 수정 필요

//        String userId = "";
//        try {
//            userId = tokenProvider.getClaims(accessToken).getSubject();
//        } catch (ExpiredJwtException ex) {
//            userId = ex.getClaims().getSubject();
//        }

        if (StringUtils.hasText(accessToken)) {
            try {
                if (tokenProvider.validateToken(accessToken)) {
                    log.info("valid accessToken");
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException accessTokenEx) {
                log.info("Expired accessToken");
                TokenDto tokenDto = tokenInfoService.getTokenInfoByAccessToken(accessToken);
                String refreshToken = tokenDto.getRefreshToken();

//                try {
//                    if (tokenProvider.validateToken(refreshToken)) {
//                        log.info("valid refreshToken");
//
//                        tokenInfoService.deleteTokenInfoByUserId(tokenDto.getUserId());
//
//                        TokenDto newTokenDto = tokenProvider.reissueToken(refreshToken);
//
//                        tokenInfoService.saveTokenInfo(
//                                TokenInfo.builder()
//                                        .userId(tokenDto.getUserId())
//                                        .accessToken(newTokenDto.getAccessToken())
//                                        .refreshToken(newTokenDto.getRefreshToken())
//                                        .build());
//
//                        Authentication authentication = tokenProvider.getAuthentication(newTokenDto.getAccessToken());
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                        CookieUtil.generateCookie((HttpServletResponse) response, "accessToken", newTokenDto.getAccessToken());
//                    }
//                } catch (ExpiredJwtException refreshTokenEx) {
//                    log.info("Expired refreshToken");
//                }
            }
        }

        chain.doFilter(request, response);
    }
}
