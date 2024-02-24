package ms.toy.my_service.domain.dto;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserInfo extends User {

    private String userId;
//    private String userName;

    public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = username;
    }

}
