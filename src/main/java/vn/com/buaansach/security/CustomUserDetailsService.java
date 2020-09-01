package vn.com.buaansach.security;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.core.repository.user.UserRepository;
import vn.com.buaansach.util.Constants;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    @Transactional
    // add transactional annotation to avoid LazyInitializationException,
    // because transaction will be closed before get we can get authorities
    public UserDetails loadUserByUsername(final String principal) {
        log.debug("Authenticating {}", principal);

        if (new EmailValidator().isValid(principal, null)) {
            return userRepository.findOneWithAuthoritiesByUserEmailIgnoreCase(principal)
                    .map(UserPrincipal::create)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND));
        }

        if (Pattern.matches(Constants.PHONE_REGEX, principal)){
            return userRepository.findOneWithAuthoritiesByUserPhone(principal)
                    .map(UserPrincipal::create)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND));
        }

        return userRepository.findOneWithAuthoritiesByUserLoginIgnoreCase(principal)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

}
