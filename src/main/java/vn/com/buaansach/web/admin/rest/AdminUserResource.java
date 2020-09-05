package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminUserService;
import vn.com.buaansach.web.admin.service.dto.read.AdminUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminPasswordChangeDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateUserDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class AdminUserResource {
    private final String ENTITY_NAME = "admin-user";
    private final Logger log = LoggerFactory.getLogger(AdminUserResource.class);
    private final AdminUserService adminUserService;

    /**
     * Admin create new user
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminUserDTO> createUser(@Valid @RequestBody AdminCreateUserDTO payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminUserService.createUser(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminUserDTO> updateUser(@Valid @RequestBody AdminUpdateUserDTO payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminUserService.updateUser(payload));
    }

    /**
     * Admin get page user
     */
    @GetMapping("/list")
    public ResponseEntity<Page<AdminUserDTO>> getPageUser(@RequestParam(value = "search", defaultValue = "") String search,
                                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "20") int size,
                                                          @RequestParam(value = "sortField", defaultValue = "createdDate") String sortField,
                                                          @RequestParam(value = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection) {
        PageRequest request = PageRequest.of(page - 1, size, sortDirection, sortField);
        log.debug("REST request from user [{}] to list [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, request);
        return ResponseEntity.ok(adminUserService.getPageUser(request, search));
    }

    /**
     * Admin change password for user
     */
    @PutMapping("/change-password")
    public void changePassword(@Valid @RequestBody AdminPasswordChangeDTO payload) {
        log.debug("REST request from user [{}] to change password for [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminUserService.adminChangePassword(payload);
    }

    /**
     * Admin activate/deactivate user
     */
    @PutMapping("/toggle-activation")
    public void toggleActivation(@RequestBody String login) {
        log.debug("REST request from user [{}] to toggle activation for [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, login);
        adminUserService.toggleActivation(login);
    }
}
