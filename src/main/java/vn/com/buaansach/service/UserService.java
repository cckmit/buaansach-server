package vn.com.buaansach.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.AuthorityEntity;
import vn.com.buaansach.entity.UserEntity;
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

    public UserEntity createUser(CreateAccountDTO dto) {
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setFirstName(dto.getFirstName());
            newUserEntity.setLastName(dto.getLastName());
            newUserEntity.setLogin(dto.getLogin().toLowerCase());
            if (dto.getEmail() != null) {
                newUserEntity.setEmail(dto.getEmail().toLowerCase());
            }
            newUserEntity.setPhone(dto.getPhone());
            newUserEntity.setActivated(dto.getActivated());
            if (dto.getLangKey() == null || dto.getLangKey().isEmpty()) {
                newUserEntity.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
            } else {
                newUserEntity.setLangKey(dto.getLangKey());
            }
            if (dto.getAuthorities() != null) {
                Set<AuthorityEntity> authorities = dto.getAuthorities().stream()
                        .map(authorityRepository::findByName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
                newUserEntity.setAuthorities(authorities);
            }
            /* if enable send creation mail => generate random password */
            if (Boolean.parseBoolean(sendCreationMail)) {
                newUserEntity.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
                newUserEntity.setResetKey(RandomUtil.generateResetKey());
                newUserEntity.setResetDate(Instant.now());
                mailService.sendCreationEmail(newUserEntity);
            } else {
                newUserEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            userRepository.save(newUserEntity);
            log.debug("Created Information for user: {}", newUserEntity);
            return newUserEntity;
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

    public Optional<UserEntity> requestPasswordReset(String mail) {
        Optional<UserEntity> optionalUser = userRepository.findOneByEmailIgnoreCase(mail).filter(UserEntity::isActivated);
        if (optionalUser.isPresent()) {
            log.debug("{} request a password reset", mail);
            UserEntity userEntity = optionalUser.get();
            userEntity.setResetKey(RandomUtil.generateResetKey());
            userEntity.setResetDate(Instant.now());
            userRepository.save(userEntity);
            return optionalUser;
        } else return Optional.empty();
    }

    public Optional<UserEntity> completePasswordReset(String newPassword, String key) {
        Optional<UserEntity> optionalUser = userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)));
        if (optionalUser.isPresent()) {
            log.debug("Reset user password for reset key: {}", key);
            UserEntity userEntity = optionalUser.get();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setResetKey(null);
            userEntity.setResetDate(null);
            userRepository.save(userEntity);
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
