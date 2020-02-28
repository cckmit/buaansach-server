package vn.com.buaansach.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.Authority;
import vn.com.buaansach.entity.User;
import vn.com.buaansach.exception.EmailAlreadyUsedException;
import vn.com.buaansach.exception.InvalidPasswordException;
import vn.com.buaansach.exception.LoginAlreadyUsedException;
import vn.com.buaansach.repository.AuthorityRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.dto.auth.AdminPasswordChangeDTO;
import vn.com.buaansach.service.dto.auth.CreateAccountDTO;
import vn.com.buaansach.service.dto.auth.UpdateAccountDTO;
import vn.com.buaansach.service.util.Constants;
import vn.com.buaansach.service.util.RandomUtil;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final MailService mailService;

    @Value("${app.mail.send-creation-mail}")
    private String sendCreationMail;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.mailService = mailService;
    }

    public User createUser(CreateAccountDTO dto) {
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = new User();
            newUser.setFirstName(dto.getFirstName());
            newUser.setLastName(dto.getLastName());
            newUser.setLogin(dto.getLogin().toLowerCase());
            if (dto.getEmail() != null) {
                newUser.setEmail(dto.getEmail().toLowerCase());
            }
            newUser.setPhone(dto.getPhone());
            newUser.setActivated(dto.getActivated());
            if (dto.getLangKey() == null || dto.getLangKey().isEmpty()) {
                newUser.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
            } else {
                newUser.setLangKey(dto.getLangKey());
            }
            if (dto.getAuthorities() != null) {
                Set<Authority> authorities = dto.getAuthorities().stream()
                        .map(authorityRepository::findByName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
                newUser.setAuthorities(authorities);
            }
            /* if enable send creation mail => generate random password */
            if (Boolean.parseBoolean(sendCreationMail)) {
                newUser.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
                newUser.setResetKey(RandomUtil.generateResetKey());
                newUser.setResetDate(Instant.now());
                mailService.sendCreationEmail(newUser);
            } else {
                newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            userRepository.save(newUser);
            log.debug("Created Information for user: {}", newUser);
            return newUser;
        }
    }

    public void updateUser(UpdateAccountDTO dto, MultipartFile image) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            if (image != null) {
                /*handle upload image here*/
            }
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setLangKey(dto.getLangKey());
            userRepository.save(user);
            log.debug("Update Information for user: {}", user.getLogin());
        });

    }

    public void adminChangePassword(AdminPasswordChangeDTO dto) {
        userRepository.findOneByLogin(dto.getLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            log.debug("Admin has changed password for user: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new InvalidPasswordException();
            }
            String newEncryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(newEncryptedPassword);
            userRepository.save(user);
            log.debug("Changed password for user: {}", user);
        });
    }

    public Optional<User> requestPasswordReset(String mail) {
        Optional<User> optionalUser = userRepository.findOneByEmailIgnoreCase(mail).filter(User::isActivated);
        if (optionalUser.isPresent()) {
            log.debug("{} request a password reset", mail);
            User user = optionalUser.get();
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(Instant.now());
            userRepository.save(user);
            return optionalUser;
        } else return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        Optional<User> optionalUser = userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)));
        if (optionalUser.isPresent()) {
            log.debug("Reset user password for reset key: {}", key);
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetKey(null);
            user.setResetDate(null);
            userRepository.save(user);
            return optionalUser;
        } else return Optional.empty();
    }

    public void toggleActivation(String login) {
        userRepository.findOneByLogin(login).ifPresent(
                user -> {
                    user.setActivated(!user.isActivated());
                    userRepository.save(user);
                }
        );
        log.debug("Toggle activation for user: {}", login);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(
                user -> {
                    user.setDeleted(true);
                    userRepository.save(user);
                }
        );
        log.debug("Delete user: {}", login);
    }
}
