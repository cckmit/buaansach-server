package vn.com.buaansach.web.general.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.BadRequestException;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;
import vn.com.buaansach.web.general.repository.user.GeneralUserRepository;
import vn.com.buaansach.web.general.service.dto.write.UpdateAccountDTO;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GeneralUserRepository generalUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    public void updateAccount(UpdateAccountDTO dto, MultipartFile image) {
        generalUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            if (dto.getUserEmail() != null) {
                Optional<UserEntity> optionalEmail = generalUserRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail());
                if (optionalEmail.isPresent() && !optionalEmail.get().getUserLogin().equals(user.getUserLogin())) {
                    throw new BadRequestException(ErrorCode.EMAIL_EXIST);
                }
            }

            if (dto.getUserPhone() != null) {
                Optional<UserEntity> optionalPhone = generalUserRepository.findOneByUserPhone(dto.getUserPhone());
                if (optionalPhone.isPresent() && !optionalPhone.get().getUserLogin().equals(user.getUserLogin())) {
                    throw new BadRequestException(ErrorCode.PHONE_EXIST);
                }
            }
            user.setUserEmail(dto.getUserEmail());
            user.setUserPhone(dto.getUserPhone());

            UserProfileEntity profileEntity = user.getUserProfile();
            if (image != null) {
                fileService.deleteByUrl(profileEntity.getAvatarUrl());
                /*handle upload image here*/
                profileEntity.setAvatarUrl(fileService.uploadImage(image, Constants.USER_IMAGE_PATH).getUrl());
            }

            profileEntity.setFullName(dto.getFullName());
            profileEntity.setUserGender(dto.getUserGender());
            profileEntity.setUserBirthday(dto.getUserBirthday());
            profileEntity.setUserAddress(dto.getUserAddress());
            profileEntity.setLangKey(dto.getLangKey());

            user.setUserProfile(profileEntity);
            generalUserRepository.save(user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        generalUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String currentEncryptedPassword = user.getUserPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new BadRequestException(ErrorCode.INCORRECT_CURRENT_PASSWORD);
            }
            String newEncryptedPassword = passwordEncoder.encode(newPassword);
            user.setUserPassword(newEncryptedPassword);
            generalUserRepository.save(user);
        });
    }

    public Optional<UserEntity> requestPasswordReset(String mail) {
        Optional<UserEntity> optionalUser = generalUserRepository.findOneByUserEmailIgnoreCase(mail).filter(UserEntity::isUserActivated);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setResetKey(RandomUtil.generateResetKey());
            userEntity.setResetDate(Instant.now());
            generalUserRepository.save(userEntity);
            return optionalUser;
        } else return Optional.empty();
    }

    public void completePasswordReset(String newPassword, String key) {
        Optional<UserEntity> optionalUser = generalUserRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)));
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setUserPassword(passwordEncoder.encode(newPassword));
            userEntity.setResetKey(null);
            userEntity.setResetDate(null);
            generalUserRepository.save(userEntity);
        } else {
            throw new NotFoundException(ErrorCode.RESET_KEY_NOT_FOUND_OR_EXPIRED);
        }
    }

}
