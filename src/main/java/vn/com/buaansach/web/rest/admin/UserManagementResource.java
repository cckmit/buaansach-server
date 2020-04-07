package vn.com.buaansach.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.admin.UserManagementService;
import vn.com.buaansach.service.dto.UserDTO;
import vn.com.buaansach.service.dto.auth.AdminPasswordChangeDTO;
import vn.com.buaansach.service.dto.auth.CreateAccountDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/management/user")
public class UserManagementResource {
    private final String ENTITY_NAME = "user";
    private final Logger log = LoggerFactory.getLogger(UserManagementResource.class);
    private final UserManagementService userManagementService;

    public UserManagementResource(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    /**
     * Admin create new user
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateAccountDTO dto) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, dto);
        UserDTO responseDto = new UserDTO(userManagementService.createUser(dto));
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Admin get page user
     */
    @GetMapping("/list")
    public ResponseEntity<Page<UserDTO>> getPageUser(@RequestParam(value = "search", defaultValue = "") String search,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "20") int size,
                                                     @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                     @RequestParam(value = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user {} to get page {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(userManagementService.getPageUser(request, search).map(UserDTO::new));
    }

    /**
     * Admin change password for user
     */
    @PutMapping("/change-password")
    public void changePassword(@Valid @RequestBody AdminPasswordChangeDTO dto) {
        log.debug("REST request from user {} to change password for {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, dto);
        userManagementService.adminChangePassword(dto);
    }

    /**
     * Admin activate/deactivate user
     */
    @PutMapping("/toggle-activation")
    public void toggleActivation(@RequestBody String login) {
        log.debug("REST request from user {} to toggle-activation for {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, login);
        userManagementService.toggleActivation(login);
    }
}
