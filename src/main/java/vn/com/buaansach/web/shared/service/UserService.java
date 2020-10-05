package vn.com.buaansach.web.shared.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.enumeration.Gender;
import vn.com.buaansach.entity.enumeration.UserType;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.util.StringUtil;
import vn.com.buaansach.web.shared.repository.user.UserProfileRepository;
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.web.shared.service.dto.write.UpdateAccountDTO;
import vn.com.buaansach.web.shared.service.dto.write.UserRegisterDTO;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    private final CustomerService customerService;

    private final CodeService codeService;

    @Transactional
    public void updateAccount(UpdateAccountDTO dto, MultipartFile image) {
        userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            if (dto.getUserEmail() != null) {
                Optional<UserEntity> optionalEmail = userRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail());
                if (optionalEmail.isPresent() && !optionalEmail.get().getUserLogin().equals(user.getUserLogin())) {
                    throw new BadRequestException(ErrorCode.EMAIL_EXIST);
                }
            }

            user.setUserEmail(dto.getUserEmail());
            userRepository.save(user);

            UserProfileEntity profileEntity = userProfileRepository.findOneByUserGuid(user.getGuid())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
            if (image != null) {
                fileService.deleteByUrl(profileEntity.getAvatarUrl());
                /*handle upload image here*/
                profileEntity.setAvatarUrl(fileService.uploadImage(image, Constants.USER_IMAGE_PATH).getUrl());
            }

            if (dto.getFullName() != null) profileEntity.setFullName(dto.getFullName());
            else throw new BadRequestException(ErrorCode.BAD_REQUEST);

            if (dto.getUserGender() != null) profileEntity.setUserGender(dto.getUserGender());
            else profileEntity.setUserGender(Gender.UNDEFINED);

            if (dto.getUserBirthday() != null) profileEntity.setUserBirthday(dto.getUserBirthday());

            if (dto.getUserAddress() != null) profileEntity.setUserAddress(dto.getUserAddress());

            if (dto.getLangKey() != null) profileEntity.setLangKey(dto.getLangKey());
            else profileEntity.setLangKey(Constants.DEFAULT_LANGUAGE);

            userProfileRepository.save(profileEntity);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String currentEncryptedPassword = user.getUserPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new BadRequestException(ErrorCode.INCORRECT_CURRENT_PASSWORD);
            }
            String newEncryptedPassword = passwordEncoder.encode(newPassword);
            user.setUserPassword(newEncryptedPassword);
            userRepository.save(user);
        });
    }

    public Optional<UserEntity> requestPasswordReset(String mail) {
        Optional<UserEntity> optionalUser = userRepository.findOneByUserEmailIgnoreCase(mail).filter(UserEntity::isUserActivated);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setResetKey(RandomUtil.generateResetKey());
            userEntity.setResetDate(Instant.now());
            userRepository.save(userEntity);
            return optionalUser;
        } else return Optional.empty();
    }

    public void completePasswordReset(String newPassword, String key) {
        Optional<UserEntity> optionalUser = userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)));
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setUserPassword(passwordEncoder.encode(newPassword));
            userEntity.setResetKey(null);
            userEntity.setResetDate(null);
            userRepository.save(userEntity);
        } else {
            throw new NotFoundException(ErrorCode.RESET_KEY_NOT_FOUND_OR_EXPIRED);
        }
    }

    public void registerUser(UserRegisterDTO dto) {
        if (dto.getUserLogin() == null || dto.getUserEmail() == null || dto.getUserPhone() == null) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (userRepository.findOneByUserLoginIgnoreCase(dto.getUserLogin().toLowerCase()).isPresent()) {
            throw new BadRequestException(ErrorCode.LOGIN_EXIST);
        }
        if (userRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail()).isPresent()) {
            throw new BadRequestException(ErrorCode.EMAIL_EXIST);
        }
        if (userRepository.findOneByUserPhone(dto.getUserPhone()).isPresent()) {
            throw new BadRequestException(ErrorCode.PHONE_EXIST);
        }

        UserEntity userEntity = new UserEntity();
        UUID userGuid = UUID.randomUUID();
        userEntity.setGuid(userGuid);
        userEntity.setUserLogin(dto.getUserLogin().toLowerCase());
        userEntity.setUserEmail(dto.getUserEmail().toLowerCase());
        userEntity.setUserPhone(dto.getUserPhone());

        userEntity.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        userEntity.setUserActivated(true);
        userEntity.setUserType(UserType.CUSTOMER);

        Set<AuthorityEntity> authorities = new HashSet<>();
        authorities.add(new AuthorityEntity(AuthoritiesConstants.CUSTOMER));
        userEntity.setAuthorities(authorities);

        UserProfileEntity profileEntity = new UserProfileEntity();
        profileEntity.setUserCode(codeService.generateCodeForCustomerUser());
        profileEntity.setFullName(dto.getFullName());
        profileEntity.setUserGuid(userGuid);
        profileEntity.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        userRepository.save(userEntity);
        userProfileRepository.save(profileEntity);
        customerService.createdCustomer(userGuid);
    }

    public boolean checkPhoneExist(String userPhone) {
        return userRepository.findOneByUserPhone(userPhone).isPresent();
    }

    public boolean checkEmailExist(String userEmail) {
        return userRepository.findOneByUserEmailIgnoreCase(userEmail.toLowerCase()).isPresent();
    }

    public boolean checkLoginExist(String userLogin) {
        return userRepository.findOneByUserLoginIgnoreCase(userLogin.toLowerCase()).isPresent();
    }

    public String getSuggestedUserLogin(String fullName) {
        int tryCount = 0;
        String prefix = fullName.toLowerCase().replaceAll("\\s+", "");
        prefix = StringUtil.normalizeString(prefix);
        boolean validUserLogin = false;
        String suffix = RandomStringUtils.randomNumeric(3);
        while (!validUserLogin && tryCount < 100) {
            if (userRepository.findOneByUserLoginIgnoreCase(prefix + suffix).isEmpty()) {
                validUserLogin = true;
            }
            suffix = RandomStringUtils.randomNumeric(3);
            tryCount++;
        }

        if (validUserLogin) return prefix + suffix;
        else return "";
    }

}
