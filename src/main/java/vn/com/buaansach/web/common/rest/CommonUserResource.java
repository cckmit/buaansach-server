package vn.com.buaansach.web.common.rest;

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
import vn.com.buaansach.exception.ErrorCode;
import vn.com.buaansach.exception.NotFoundException;
import vn.com.buaansach.security.jwt.TokenProvider;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.common.repository.CommonUserRepository;
import vn.com.buaansach.web.common.service.UserService;
import vn.com.buaansach.web.common.service.MailService;
import vn.com.buaansach.web.common.service.dto.read.JwtTokenDTO;
import vn.com.buaansach.web.common.service.dto.read.UserDTO;
import vn.com.buaansach.web.common.service.dto.write.LoginRequestDTO;
import vn.com.buaansach.web.common.service.dto.write.PasswordResetDTO;
import vn.com.buaansach.web.common.service.dto.write.UpdateAccountDTO;
import vn.com.buaansach.web.common.service.dto.write.UserPasswordChangeDTO;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class CommonUserResource {
    private final String ENTITY_NAME = "user";
    private final Logger log = LoggerFactory.getLogger(CommonUserResource.class);

    private final MailService mailService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final CommonUserRepository commonUserRepository;

    public CommonUserResource(MailService mailService, UserService userService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, CommonUserRepository commonUserRepository) {
        this.mailService = mailService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.commonUserRepository = commonUserRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDTO> authenticate(@Valid @RequestBody LoginRequestDTO dto) {
        log.debug("REST request to authenticate [{}] : [{}]", ENTITY_NAME, dto.getPrincipal());
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
        return commonUserRepository.findOneByUserLoginIgnoreCase(SecurityUtils.getCurrentUserLogin())
                .map(userEntity -> ResponseEntity.ok(new UserDTO(userEntity)))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @GetMapping("/info/{userLogin}")
    public ResponseEntity<UserDTO> getAccountInfoByLogin(@PathVariable String userLogin) {
        log.debug("REST request from user [{}] to get [{}] info by login [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, userLogin);
        return commonUserRepository.findOneByUserLoginIgnoreCase(userLogin)
                .map(userEntity -> ResponseEntity.ok(new UserDTO(userEntity)))
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
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
    public ResponseEntity<Void> requestPasswordReset(@RequestBody String email) {
        email = email.replace("\"", "");
        Optional<UserEntity> user = userService.requestPasswordReset(email);
        if (user.isPresent()) {
            log.debug("REST request to reset password for email : [{}]", email);
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail : [{}]", email);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@Valid @RequestBody PasswordResetDTO dto) {
        log.debug("REST request to complete password reset with reset key: [{}]", dto.getKey());
        userService.completePasswordReset(dto.getNewPassword(), dto.getKey());
        return ResponseEntity.noContent().build();
    }
}
