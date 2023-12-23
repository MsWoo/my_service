package mswoo.toyproject.my_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import mswoo.toyproject.my_service.util.IpUtil;
import mswoo.toyproject.my_service.wrapper.CustomRequestWrapper;
import mswoo.toyproject.my_service.wrapper.CustomResponseWrapper;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * 로깅 필터 구현
 */
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    /**
     * logback MDC 이용
     * traceID 를 이용 -> request-response 추적 용이
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString());
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(new CustomRequestWrapper(request), new CustomResponseWrapper(response), filterChain);
        }
        MDC.clear();
    }

    protected void doFilterWrapped(CustomRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response);
            response.copyBodyToResponse();
        }
    }

    /**
     * Request 로그
     * @param request
     * @throws IOException
     */
    private static void logRequest(CustomRequestWrapper request) throws IOException {
        String queryString = request.getQueryString();
        log.info("Request: {} uri=[{}] content-type=[{}] client-ip=[{}]",
                request.getMethod(),
                queryString == null ? request.getRequestURI() : request.getRequestURI() + "?" + queryString,
                request.getContentType(),
                IpUtil.getClientIp(request)
        );
        logPayload("Request", request.getContentType(), request.getInputStream());
    }

    /**
     * Response 로그
     * @param response
     * @throws IOException
     */
    private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
        logPayload("Response", response.getContentType(), response.getContentInputStream());
    }

    /**
     * Payload 로그
     * @param prefix
     * @param contentType
     * @param inputStream
     * @throws IOException
     */
    private static void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                String contentString = new String(content);
                log.info("{} Payload: {}", prefix, contentString);
            }
        } else {
            log.info("{} Payload: Binary Content", prefix);
        }
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml")
        );
        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
