package vn.com.buaansach.service.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.model.entity.AuthorityEntity;
import vn.com.buaansach.model.entity.UserEntity;
import vn.com.buaansach.exception.EmailAlreadyUsedException;
import vn.com.buaansach.exception.LoginAlreadyUsedException;
import vn.com.buaansach.exception.PhoneAlreadyUsedException;
import vn.com.buaansach.repository.AuthorityRepository;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.service.FileService;
import vn.com.buaansach.service.MailService;
import vn.com.buaansach.model.dto.auth.AdminPasswordChangeDTO;
import vn.com.buaansach.model.dto.auth.CreateAccountDTO;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final MailService mailService;

    private final FileService fileService;

    @Value("${app.mail.send-creation-mail}")
    private String sendCreationMail;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, MailService mailService, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.mailService = mailService;
        this.fileService = fileService;
    }

    public UserEntity createUser(CreateAccountDTO dto) {
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else if (userRepository.findOneByPhone(dto.getPhone()).isPresent()) {
            throw new PhoneAlreadyUsedException();
        } else {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setFirstName(dto.getFirstName());
            newUserEntity.setLastName(dto.getLastName());
            newUserEntity.setLogin(dto.getLogin().toLowerCase());
            if (dto.getEmail() != null) {
                newUserEntity.setEmail(dto.getEmail().toLowerCase());
            }
            newUserEntity.setPhone(dto.getPhone());
            newUserEntity.setActivated(dto.isActivated());
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
            return newUserEntity;
        }
    }

    public Page<UserEntity> getPageUser(PageRequest request, String search) {
        return userRepository.findPageStoreWithKeyword(request, search.toLowerCase());
    }

    public void adminChangePassword(AdminPasswordChangeDTO dto) {
        userRepository.findOneByLogin(dto.getLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        });
    }


    public void toggleActivation(String login) {
        userRepository.findOneByLogin(login).ifPresent(
                user -> {
                    user.setActivated(!user.isActivated());
                    userRepository.save(user);
                }
        );
    }

}
