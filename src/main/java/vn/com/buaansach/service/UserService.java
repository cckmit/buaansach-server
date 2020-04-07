package vn.com.buaansach.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.model.entity.UserEntity;
import vn.com.buaansach.exception.EmailAlreadyUsedException;
import vn.com.buaansach.exception.InvalidPasswordException;
import vn.com.buaansach.exception.PhoneAlreadyUsedException;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.model.dto.auth.UpdateAccountDTO;
import vn.com.buaansach.util.Constants;
import vn.com.buaansach.util.RandomUtil;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
    }

    public void updateUser(UpdateAccountDTO dto, MultipartFile image) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            if (image != null) {
                /*handle upload image here*/
                user.setImageUrl(fileService.uploadImage(image, Constants.USER_IMAGE_PATH).getUrl());
            }

            Optional<UserEntity> optionalEmail = userRepository.findOneByEmailIgnoreCase(dto.getEmail());
            Optional<UserEntity> optionalPhone = userRepository.findOneByPhone(dto.getPhone());
            if (optionalEmail.isPresent()) {
                if (!optionalEmail.get().getLogin().equals(user.getLogin())) throw new EmailAlreadyUsedException();
            }

            if (optionalPhone.isPresent()) {
                if (!optionalPhone.get().getLogin().equals(user.getLogin())) throw new PhoneAlreadyUsedException();
            }

            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setLangKey(dto.getLangKey());
            userRepository.save(user);
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
        });
    }

    public Optional<UserEntity> requestPasswordReset(String mail) {
        Optional<UserEntity> optionalUser = userRepository.findOneByEmailIgnoreCase(mail).filter(UserEntity::isActivated);
        if (optionalUser.isPresent()) {
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
            UserEntity userEntity = optionalUser.get();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setResetKey(null);
            userEntity.setResetDate(null);
            userRepository.save(userEntity);
            return optionalUser;
        } else return Optional.empty();
    }

}
