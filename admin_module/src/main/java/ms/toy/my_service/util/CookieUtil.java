package ms.toy.my_service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;

public class CookieUtil {
    private static final int COOKIE_EXPIRATION_TIME = 3 * 60 * 60;

    public static String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (!ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void generateCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_EXPIRATION_TIME);
        response.addCookie(cookie);
    }

}
