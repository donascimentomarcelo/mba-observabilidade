package store.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
//@RequiredArgsConstructor
class RequestMdcInterceptor implements HandlerInterceptor {

    private final BuildProperties buildProperties;

    public RequestMdcInterceptor(@Autowired(required = false) final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        if (buildProperties != null) {
            MDC.put("appName", buildProperties.getName());
            MDC.put("appVersion", buildProperties.getVersion());
            MDC.put("appBuildDate", buildProperties.getTime().toString());
        } else {
            MDC.put("appName", "store-api");
            MDC.put("appVersion", "unknown");
            MDC.put("appBuildDate", "unknown");
        }

        MDC.put("traceId", request.getHeader("x-trace-id"));
        MDC.put("host", request.getHeader("Host"));
        return true;
    }
}
