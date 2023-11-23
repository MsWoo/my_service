package mswoo.toyproject.my_service.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mswoo.toyproject.my_service.domain.dto.TokenDto;
import mswoo.toyproject.my_service.domain.entity.TokenInfo;
import mswoo.toyproject.my_service.service.TokenInfoService;
import mswoo.toyproject.my_service.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenInfoService tokenInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 쿠키 or 헤더에서 토큰 추출
        String accessToken = CookieUtil.getAccessToken((HttpServletRequest) request);
        if (ObjectUtils.isEmpty(accessToken)) {
            accessToken = tokenProvider.resolveToken((HttpServletRequest) request);
        }

        if (StringUtils.hasText(accessToken)) {
            try {
                if (tokenProvider.validateToken(accessToken)) {
                    log.info("valid accessToken");
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException accessTokenEx) {
                log.info("Expired accessToken");
                try {
                    String refreshToken = tokenInfoService.getTokenInfoById(accessToken).getRefreshToken();

                    if (tokenProvider.validateToken(refreshToken)) {
                        log.info("valid refreshToken");

                        tokenInfoService.deleteTokenInfoById(accessToken);

                        TokenDto tokenDto = tokenProvider.reissueToken(refreshToken);

                        tokenInfoService.saveTokenInfo(
                                TokenInfo.builder()
                                        .accessToken(tokenDto.getAccessToken())
                                        .refreshToken(tokenDto.getRefreshToken())
                                        .build());

                        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        CookieUtil.generateCookie((HttpServletResponse) response, "accessToken", tokenDto.getAccessToken());
                    }
                } catch (ExpiredJwtException | ResponseStatusException ex) {
                    log.info("Expired refreshToken");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
