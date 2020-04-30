package vn.com.buaansach.web.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.admin.repository.AdminAuthorityRepository;
import vn.com.buaansach.web.admin.repository.AdminUserRepository;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminPasswordChangeDTO;
import vn.com.buaansach.web.user.service.MailService;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AdminAuthorityRepository adminAuthorityRepository;

    private final MailService mailService;

    @Value("${app.mail.send-creation-mail}")
    private String sendCreationMail;

    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder, AdminAuthorityRepository adminAuthorityRepository, MailService mailService) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminAuthorityRepository = adminAuthorityRepository;
        this.mailService = mailService;
    }

    public UserEntity createUser(AdminCreateUserDTO dto) {
        if (adminUserRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (adminUserRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else if (dto.getPhone() != null && adminUserRepository.findOneByPhone(dto.getPhone()).isPresent()) {
            throw new PhoneAlreadyUsedException();
        } else {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setGuid(UUID.randomUUID());
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
                        .map(adminAuthorityRepository::findByName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
                newUserEntity.setAuthorities(authorities);
            } else {
                Set<AuthorityEntity> authorities = new HashSet<>();
                authorities.add(new AuthorityEntity(AuthoritiesConstants.USER));
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
            adminUserRepository.save(newUserEntity);
            return newUserEntity;
        }
    }

    public Page<UserEntity> getPageUser(PageRequest request, String search) {
        return adminUserRepository.findPageUserWithKeyword(request, search.toLowerCase());
    }

    public void adminChangePassword(AdminPasswordChangeDTO dto) {
        UserEntity userEntity = adminUserRepository.findOneByLogin(dto.getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + dto.getLogin()));
        String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
        userEntity.setPassword(encryptedPassword);
        adminUserRepository.save(userEntity);
    }

    public void toggleActivation(String login) {
        if (SecurityUtils.getCurrentUserLogin().equals(login))
            throw new AccessDeniedException("You cannot deactivate your account");

        UserEntity userEntity = adminUserRepository.findOneByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));

        userEntity.setDisabledByAdmin(userEntity.isActivated());
        userEntity.setActivated(!userEntity.isActivated());
        adminUserRepository.save(userEntity);
    }

}
