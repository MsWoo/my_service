package ms.toy.my_service.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import ms.toy.my_service.domain.dto.MemberInfo;
import ms.toy.my_service.domain.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class TokenProvider {
    private SecretKey secretKey;
    private String expirationTime;
    private String refreshExpirationTime;

    public TokenProvider(
            @Value("${jwt.secret-key}") String key,
            @Value("${jwt.expiration-time}") String expirationTime,
            @Value("${jwt.refresh-expiration-time}") String refreshExpirationTime) {
        String secret = Base64.getEncoder().encodeToString(key.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public TokenDto generateToken(Authentication authentication) {
        Date now = new Date();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", authorities)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.expirationTime)))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", authorities)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.refreshExpirationTime)))
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto reissueToken(String token) {
        Date now = new Date();
        Claims claims = this.getClaims(token);

        String accessToken = Jwts.builder()
                .setSubject(claims.getSubject())
                .claim("role", claims.get("role"))
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.expirationTime)))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(claims.getSubject())
                .claim("role", claims.get("role"))
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.refreshExpirationTime)))
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
        } catch (UnsupportedJwtException e) {
            log.info("JWT token not supported.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token is invalid");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object authoritiesCliam = claims.get("role");

        Collection<? extends GrantedAuthority> authorities = (authoritiesCliam == null) ?
                AuthorityUtils.NO_AUTHORITIES : AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("role").toString());

        // 로그인 API 제외 모든 요청을 거치는 JWT 필터에서 SecurityContextHolder에 설정할 Authentication 객체에 principal로 memberInfo를 넣어준다.
        // 컨트롤러에서 @AuthenticationPrincipal 어노테이션을 통해 해당 memberInfo 객체를 가져와서 사용 가능하다.
        MemberInfo memberInfo = new MemberInfo(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(memberInfo, token, authorities);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
