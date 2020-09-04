package vn.com.buaansach.web.shared.service;

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
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.web.shared.service.dto.write.UpdateAccountDTO;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    public void updateAccount(UpdateAccountDTO dto, MultipartFile image) {
        userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            if (dto.getUserEmail() != null) {
                Optional<UserEntity> optionalEmail = userRepository.findOneByUserEmailIgnoreCase(dto.getUserEmail());
                if (optionalEmail.isPresent() && !optionalEmail.get().getUserLogin().equals(user.getUserLogin())) {
                    throw new BadRequestException(ErrorCode.EMAIL_EXIST);
                }
            }

            if (dto.getUserPhone() != null) {
                Optional<UserEntity> optionalPhone = userRepository.findOneByUserPhone(dto.getUserPhone());
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
            userRepository.save(user);
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

}
