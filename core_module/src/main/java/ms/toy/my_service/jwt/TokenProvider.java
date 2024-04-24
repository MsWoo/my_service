package ms.toy.my_service.jwt;

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
import ms.toy.my_service.domain.dto.TokenDto;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.domain.entity.Users;
import ms.toy.my_service.enums.SiteType;
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

    public TokenDto generateToken(MemberInfo memberInfo) {
        if (SiteType.ADMIN.equals(memberInfo.getSiteType())) {
            Admin admin = (Admin) memberInfo.getMember();
            return this.generateToken(memberInfo.getAuthorities(), memberInfo.getUsername(), admin.getId(), admin.getUserName());
        } else {
            Users user = (Users) memberInfo.getMember();
            return this.generateToken(memberInfo.getAuthorities(), memberInfo.getUsername(), user.getId(), user.getUserName());
        }
    }

    private TokenDto generateToken(Collection<? extends GrantedAuthority> permissions, String userId, Long id, String username) {
        Date now = new Date();

        String authorities = permissions.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim(JwtAttributes.MEMBERSEQ_KEY, id)
                .claim(JwtAttributes.MEMBERNAME_KEY, username)
                .claim(JwtAttributes.AUTHORITIES_KEY, authorities)
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.expirationTime)))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .claim(JwtAttributes.MEMBERSEQ_KEY, id)
                .claim(JwtAttributes.MEMBERNAME_KEY, username)
                .claim(JwtAttributes.AUTHORITIES_KEY, authorities)
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
                .claim(JwtAttributes.MEMBERSEQ_KEY, claims.get(JwtAttributes.MEMBERSEQ_KEY))
                .claim(JwtAttributes.MEMBERNAME_KEY, claims.get(JwtAttributes.MEMBERNAME_KEY))
                .claim(JwtAttributes.AUTHORITIES_KEY, claims.get(JwtAttributes.AUTHORITIES_KEY))
                .signWith(this.secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() + Long.parseLong(this.expirationTime)))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(claims.getSubject())
                .claim(JwtAttributes.MEMBERSEQ_KEY, claims.get(JwtAttributes.MEMBERSEQ_KEY))
                .claim(JwtAttributes.MEMBERNAME_KEY, claims.get(JwtAttributes.MEMBERNAME_KEY))
                .claim(JwtAttributes.AUTHORITIES_KEY, claims.get(JwtAttributes.AUTHORITIES_KEY))
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
        String bearerToken = request.getHeader(JwtAttributes.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtAttributes.BEARER_PREFIX)) {
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

        Object authoritiesCliam = claims.get(JwtAttributes.AUTHORITIES_KEY);

        Collection<? extends GrantedAuthority> authorities = (authoritiesCliam == null) ?
                AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(JwtAttributes.AUTHORITIES_KEY).toString());

        // 컨트롤러에서 @AuthenticationPrincipal 어노테이션을 통해 해당 memberInfo 객체를 가져와서 사용 가능하다.
//        MemberInfo memberInfo = new MemberInfo(claims.getSubject(), authorities);
        MemberInfo memberInfo = new MemberInfo(
                claims.getSubject(),
                authorities,
                (Integer) claims.get(JwtAttributes.MEMBERSEQ_KEY),
                String.valueOf(claims.get(JwtAttributes.MEMBERNAME_KEY)));

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
