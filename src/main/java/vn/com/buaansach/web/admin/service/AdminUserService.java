package vn.com.buaansach.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.ForbiddenException;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.sequence.UserCodeGenerator;
import vn.com.buaansach.web.admin.repository.user.AdminAuthorityRepository;
import vn.com.buaansach.web.admin.repository.user.AdminUserProfileRepository;
import vn.com.buaansach.web.admin.repository.user.AdminUserRepository;
import vn.com.buaansach.web.admin.service.dto.read.AdminUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminPasswordChangeDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateUserDTO;
import vn.com.buaansach.web.shared.service.CustomerService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserRepository adminUserRepository;
    private final AdminUserProfileRepository adminUserProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminAuthorityRepository adminAuthorityRepository;
    private final AdminCodeService adminCodeService;
    private final CustomerService customerService;

    @Transactional
    public AdminUserDTO createUser(AdminCreateUserDTO dto) {
        if (dto.getUserLogin() != null && adminUserRepository.findOneByUserLoginIgnoreCase(dto.getUserLogin().toLowerCase()).isPresent()) {
            throw new BadRequestException(ErrorCode.LOGIN_EXIST);
        }
        if (dto.getUserEmail() != null && adminUserRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail()).isPresent()) {
            throw new BadRequestException(ErrorCode.EMAIL_EXIST);
        }
        if (dto.getUserPhone() != null && adminUserRepository.findOneByUserPhone(dto.getUserPhone()).isPresent()) {
            throw new BadRequestException(ErrorCode.PHONE_EXIST);
        }

        UserEntity userEntity = new UserEntity();
        UUID userGuid = UUID.randomUUID();
        userEntity.setGuid(userGuid);
        userEntity.setUserLogin(dto.getUserLogin().toLowerCase());
        if (dto.getUserEmail() != null) {
            userEntity.setUserEmail(dto.getUserEmail().toLowerCase());
        }
        if (dto.getUserPhone() != null) {
            userEntity.setUserPhone(dto.getUserPhone());
        }

        userEntity.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        userEntity.setUserActivated(dto.isUserActivated());
        userEntity.setUserType(UserType.INTERNAL);

        if (dto.getAuthorities() != null) {
            Set<AuthorityEntity> authorities = dto.getAuthorities().stream()
                    .map(adminAuthorityRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            userEntity.setAuthorities(authorities);
        } else {
            Set<AuthorityEntity> authorities = new HashSet<>();
            authorities.add(new AuthorityEntity(AuthoritiesConstants.INTERNAL_USER));
            authorities.add(new AuthorityEntity(AuthoritiesConstants.CUSTOMER));
            userEntity.setAuthorities(authorities);
        }

        UserProfileEntity profileEntity = new UserProfileEntity();
        profileEntity.setUserCode(adminCodeService.generateCodeForUser());
        profileEntity.setFullName(dto.getFullName());
        profileEntity.setUserGuid(userGuid);
        if (dto.getLangKey() == null || dto.getLangKey().isEmpty()) {
            profileEntity.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            profileEntity.setLangKey(dto.getLangKey());
        }

        customerService.createdCustomer(userGuid);
        return new AdminUserDTO(adminUserRepository.save(userEntity), adminUserProfileRepository.save(profileEntity));
    }

    @Transactional
    public AdminUserDTO updateUser(AdminUpdateUserDTO dto) {
        UserEntity currentUser = adminUserRepository.findOneByUserLoginIgnoreCase(dto.getUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        adminUserRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail()).ifPresent(userEntity -> {
            if (!userEntity.getUserLogin().equals(currentUser.getUserLogin()))
                throw new BadRequestException(ErrorCode.EMAIL_EXIST);
        });

        adminUserRepository.findOneByUserPhone(dto.getUserPhone()).ifPresent(userEntity -> {
            if (!userEntity.getUserLogin().equals(currentUser.getUserLogin()))
                throw new BadRequestException(ErrorCode.PHONE_EXIST);
        });

        currentUser.setUserEmail(dto.getUserEmail());
        currentUser.setUserPhone(dto.getUserPhone());
        if (dto.getAuthorities() != null) {
            Set<AuthorityEntity> authorities = dto.getAuthorities().stream()
                    .map(adminAuthorityRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            currentUser.setAuthorities(authorities);
        }

        UserProfileEntity profileEntity = adminUserProfileRepository.findOneByUserGuid(currentUser.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
        profileEntity.setFullName(dto.getFullName());
        profileEntity.setLangKey(dto.getLangKey());

        return new AdminUserDTO(adminUserRepository.save(currentUser), adminUserProfileRepository.save(profileEntity));
    }

    public Page<AdminUserDTO> getPageUser(PageRequest request, String search) {
        return adminUserRepository.findPageUserWithKeyword(request, search.toLowerCase());
    }

    public void adminChangePassword(AdminPasswordChangeDTO dto) {
        UserEntity userEntity = adminUserRepository.findOneByUserLoginIgnoreCase(dto.getUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        String encryptedPassword = passwordEncoder.encode(dto.getNewPassword());
        userEntity.setUserPassword(encryptedPassword);
        adminUserRepository.save(userEntity);
    }

    public void toggleActivation(String userLogin) {
        if (SecurityUtils.getCurrentUserLogin().equals(userLogin))
            throw new ForbiddenException(ErrorCode.INVALID_OPERATION);

        UserEntity userEntity = adminUserRepository.findOneByUserLoginIgnoreCase(userLogin)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        userEntity.setUserActivated(!userEntity.isUserActivated());
        adminUserRepository.save(userEntity);
    }

}
