package ms.toy.my_service.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class CustomResponseWrapper extends ContentCachingResponseWrapper {

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
    }

}
