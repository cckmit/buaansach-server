package vn.com.buaansach.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        String uri = httpServletRequest.getRequestURI();

        String headers = "";
        Iterator<String> iter = httpServletRequest.getHeaderNames().asIterator();
        while (iter.hasNext()) {
            String item = iter.next();
            headers += item + ": " + httpServletRequest.getHeader(item) + ";";
        }

        log.error("Responding with unauthorized error. URI = [{}], Headers = [{}]. Message - {}", uri, headers, e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
