package ms.toy.my_service.jwt;

import java.util.Collection;
import lombok.Getter;
import ms.toy.my_service.enums.SiteType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberInfo<T> extends User {
    private T member;
    private Integer memberSeq;
    private String memberName;
    private SiteType siteType;

    /**
     * 로그인 성공 후 토큰 발급 시 사용
     * SiteType으로 분기해서 타입 캐스팅
     */
    public MemberInfo(String username, Collection<? extends GrantedAuthority> authorities, T member, SiteType siteType) {
        super(username, "", authorities);
        this.member = member;
        this.siteType = siteType;
    }

    /**
     * JWT 토큰에서 인증 객체 조회 시 사용
     * Member(Admin, Users)객체를 풀어서 seq, name 전달
     */
    public MemberInfo(String username, Collection<? extends GrantedAuthority> authorities, Integer memberSeq, String memberName) {
        super(username, "", authorities);
        this.memberSeq = memberSeq;
        this.memberName = memberName;
    }

}
