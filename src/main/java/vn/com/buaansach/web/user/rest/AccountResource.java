package vn.com.buaansach.web.user.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.security.jwt.TokenProvider;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.user.repository.UserRepository;
import vn.com.buaansach.web.user.service.AccountService;
import vn.com.buaansach.web.user.service.MailService;
import vn.com.buaansach.web.user.service.dto.UserDTO;
import vn.com.buaansach.web.user.service.dto.auth.*;

import javax.validation.Valid;
import java.util.Optional;

@Secured(AuthoritiesConstants.USER)
@RestController
@RequestMapping("/api/v1/account")
public class AccountResource {
    private final String ENTITY_NAME = "account";
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final MailService mailService;

    private final AccountService accountService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    public AccountResource(MailService mailService, AccountService accountService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserRepository userRepository) {
        this.mailService = mailService;
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDTO> authenticate(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication, dto.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        String tokenType = "Bearer";
        httpHeaders.add("Authorization", tokenType + " " + jwt);
        log.debug("REST request to authenticate user: [{}]", SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(new JwtTokenDTO(jwt, tokenType), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> getAccountInfo() {
        log.debug("REST request from user [{}] to get account info", SecurityUtils.getCurrentUserLogin());
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
                .map(userEntity -> ResponseEntity.ok(new UserDTO(userEntity)))
                .orElseThrow(() -> new ResourceNotFoundException("user@userNotFound@" + SecurityUtils.getCurrentUserLogin()));
    }

    @GetMapping("/info/{userLogin}")
    public ResponseEntity<UserDTO> getAccountInfoByLogin(@PathVariable String userLogin) {
        log.debug("REST request from user [{}] to get account info by login [{}]", SecurityUtils.getCurrentUserLogin(), userLogin);
        return userRepository.findOneByLogin(userLogin)
                .map(userEntity -> ResponseEntity.ok(new UserDTO(userEntity)))
                .orElseThrow(() -> new ResourceNotFoundException("user@userNotFound@" + userLogin));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateAccount(@Valid @RequestPart("payload") UpdateAccountDTO payload,
                                              @RequestPart(value = "image", required = false) MultipartFile image) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        accountService.updateAccount(payload, image);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody UserPasswordChangeDTO dto) {
        log.debug("REST request from user [{}] to change password for [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, dto);
        accountService.changePassword(dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody String email) {
        email = email.replace("\"", "");
        Optional<UserEntity> user = accountService.requestPasswordReset(email);
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
        log.debug("REST request to complete password reset : [{}]", dto);
        Optional<UserEntity> user = accountService.completePasswordReset(dto.getNewPassword(), dto.getKey());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("No user was found for this reset key: " + dto.getKey());
        }
        return ResponseEntity.noContent().build();
    }
}
