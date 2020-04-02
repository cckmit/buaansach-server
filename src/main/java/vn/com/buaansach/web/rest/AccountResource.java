package vn.com.buaansach.web.rest;

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
import vn.com.buaansach.entity.UserEntity;
import vn.com.buaansach.exception.ResourceNotFoundException;
import vn.com.buaansach.repository.UserRepository;
import vn.com.buaansach.security.jwt.TokenProvider;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.MailService;
import vn.com.buaansach.service.UserService;
import vn.com.buaansach.service.dto.UserDTO;
import vn.com.buaansach.service.dto.auth.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final MailService mailService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    public AccountResource(MailService mailService, UserService userService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserRepository userRepository) {
        this.mailService = mailService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDTO> authenticate(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication, dto.getRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        String tokenType = "Bearer";
        httpHeaders.add("Authorization", tokenType + " " + jwt);
        return new ResponseEntity<>(new JwtTokenDTO(jwt, tokenType), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDTO> getAccountInfo() {
        Optional<UserEntity> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(new UserDTO(optionalUser.get()));
        }
        throw new ResourceNotFoundException("User with login '" + SecurityUtils.getCurrentUserLogin() + "' could not be found!");
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@Valid @RequestPart("dto") UpdateAccountDTO dto,
                                       @RequestPart(value = "image", required = false) MultipartFile image) {
        userService.updateUser(dto, image);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody UserPasswordChangeDTO dto) {
        userService.changePassword(dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody String email) {
        email = email.replace("\"", "");
        Optional<UserEntity> user = userService.requestPasswordReset(email);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail '{}'", email);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@Valid @RequestBody PasswordResetDTO dto) {
        Optional<UserEntity> user = userService.completePasswordReset(dto.getNewPassword(), dto.getKey());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("No user was found for this reset key: " + dto.getKey());
        }
        return ResponseEntity.noContent().build();
    }
}
