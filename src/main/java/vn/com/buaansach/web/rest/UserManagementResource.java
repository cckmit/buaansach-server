package vn.com.buaansach.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.service.UserService;
import vn.com.buaansach.service.dto.UserDTO;
import vn.com.buaansach.service.dto.auth.AdminPasswordChangeDTO;
import vn.com.buaansach.service.dto.auth.CreateAccountDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/management/user")
public class UserManagementResource {
    private final UserService userService;

    public UserManagementResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Admin create new user
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createAccount(@Valid @RequestBody CreateAccountDTO dto) {
        UserDTO responseDto = new UserDTO(userService.createUser(dto));
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Admin change password for users
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @PutMapping("/change-password")
    public void changePassword(@Valid @RequestBody AdminPasswordChangeDTO dto) {
        userService.adminChangePassword(dto);
    }

    /**
     * Admin activate/deactivate user
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @PutMapping("/toggle-activation")
    public void toggleActivation(@RequestBody String login) {
        userService.toggleActivation(login);
    }

    /**
     * Admin delete user
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody String login) {
        userService.deleteUser(login);
    }
}
