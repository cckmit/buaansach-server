package vn.com.buaansach.web.shared.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.entity.user.UserProfileEntity;
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.jwt.TokenProvider;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.shared.repository.user.UserProfileRepository;
import vn.com.buaansach.web.shared.repository.user.UserRepository;
import vn.com.buaansach.web.shared.service.MailService;
import vn.com.buaansach.web.shared.service.UserService;
import vn.com.buaansach.web.shared.service.dto.read.JwtTokenDTO;
import vn.com.buaansach.web.shared.service.dto.read.UserDTO;
import vn.com.buaansach.web.shared.service.dto.write.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserResource {
    private final String ENTITY_NAME = "user";
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final MailService mailService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDTO payload){
        log.debug("REST request to register [{}] : [{}]", ENTITY_NAME, payload.getUserLogin());
        userService.registerUser(payload);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDTO> authenticate(@Valid @RequestBody LoginRequestDTO dto) {
        log.debug("Authenticating [{}]", dto.getPrincipal());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getPrincipal(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication, dto.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        String tokenType = "Bearer";
        httpHeaders.add("Authorization", tokenType + " " + jwt);
        return new ResponseEntity<>(new JwtTokenDTO(jwt, tokenType), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserInfo() {
        log.debug("REST request from user [{}] to get [{}] info", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        UserEntity userEntity = userRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        UserProfileEntity userProfileEntity = userProfileRepository.findOneByUserGuid(userEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
        return ResponseEntity.ok(new UserDTO(userEntity,userProfileEntity));
    }

    @GetMapping("/info/{userLogin}")
    public ResponseEntity<UserDTO> getAccountInfoByLogin(@PathVariable String userLogin) {
        log.debug("REST request from user [{}] to get [{}] info by login [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, userLogin);
        UserEntity userEntity = userRepository.findOneByUserLoginIgnoreCase(userLogin)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        UserProfileEntity userProfileEntity = userProfileRepository.findOneByUserGuid(userEntity.getGuid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_PROFILE_NOT_FOUND));
        return ResponseEntity.ok(new UserDTO(userEntity,userProfileEntity));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateAccount(@Valid @RequestPart("payload") UpdateAccountDTO payload,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        userService.updateAccount(payload, image);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody UserPasswordChangeDTO dto) {
        log.debug("REST request from user [{}] to change password", SecurityUtils.getCurrentUserLogin());
        userService.changePassword(dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody ResetPasswordDTO payload) {
        Optional<UserEntity> user = userService.requestPasswordReset(payload.getEmail());
        if (user.isPresent()) {
            log.debug("REST request to reset password for email : [{}]", payload);
            mailService.sendPasswordResetMail(user.get(), payload.getDomainType());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail : [{}]", payload);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@Valid @RequestBody PasswordResetDTO dto) {
        log.debug("REST request to complete password reset with reset key: [{}]", dto.getKey());
        userService.completePasswordReset(dto.getNewPassword(), dto.getKey());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-phone-exist")
    public ResponseEntity<Boolean> checkPhoneExist(@RequestParam("userPhone") String userPhone){
        return ResponseEntity.ok(userService.checkPhoneExist(userPhone));
    }

    @GetMapping("/check-email-exist")
    public ResponseEntity<Boolean> checkEmailExist(@RequestParam("userEmail") String userEmail){
        return ResponseEntity.ok(userService.checkEmailExist(userEmail));
    }

    @GetMapping("/check-login-exist")
    public ResponseEntity<Boolean> checkLoginExist(@RequestParam("userLogin") String userLogin){
        return ResponseEntity.ok(userService.checkLoginExist(userLogin));
    }

    @GetMapping("/get-suggested-login")
    public ResponseEntity<String> getSuggestedUserLogin(@RequestParam("fullName") String fullName){
        return ResponseEntity.ok(userService.getSuggestedUserLogin(fullName));
    }
}
