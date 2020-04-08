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
import vn.com.buaansach.service.dto.UserDTO;
import vn.com.buaansach.service.dto.auth.AdminPasswordChangeDTO;
import vn.com.buaansach.service.dto.auth.CreateAccountDTO;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.service.admin.AdminUserService;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminUserResource {
    private final String ENTITY_NAME = "user";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminUserService adminUserService;

    public AdminUserResource(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * Admin create new user
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateAccountDTO payload) {
        log.debug("REST request from user {} to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        UserDTO responseDto = new UserDTO(adminUserService.createUser(payload));
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
        log.debug("REST request from user {} to list {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(adminUserService.getPageUser(request, search).map(UserDTO::new));
    }

    /**
     * Admin change password for user
     */
    @PutMapping("/change-password")
    public void changePassword(@Valid @RequestBody AdminPasswordChangeDTO payload) {
        log.debug("REST request from user {} to change password for {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminUserService.adminChangePassword(payload);
    }

    /**
     * Admin activate/deactivate user
     */
    @PutMapping("/toggle-activation")
    public void toggleActivation(@RequestBody String login) {
        log.debug("REST request from user {} to toggle activation for {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, login);
        adminUserService.toggleActivation(login);
    }
}
