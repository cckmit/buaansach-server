package vn.com.buaansach.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.util.Constants;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentOptionalUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
    }
}
