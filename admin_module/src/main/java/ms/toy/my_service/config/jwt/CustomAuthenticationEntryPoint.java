package ms.toy.my_service.config.jwt;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import ms.toy.my_service.domain.dto.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // CustomUserDetailsService의 Exception 처리
        if (authException instanceof UsernameNotFoundException || authException instanceof BadCredentialsException) {
            response.getWriter().write(
                    new Gson().toJson(new ErrorResponse(String.valueOf(HttpServletResponse.SC_BAD_REQUEST), authException.getMessage())));
        }
        else {
            response.getWriter().write(
                    new Gson().toJson(new ErrorResponse(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), "로그인이 필요합니다.")));
        }
    }
}
