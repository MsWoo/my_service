package ms.toy.my_service.config.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomDetailsSerivce {
    UserDetails loadUserByUsername(String username, String password) throws UsernameNotFoundException;
}
