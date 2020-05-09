package vn.com.buaansach.web.admin.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreUserService;
import vn.com.buaansach.web.admin.service.dto.write.AdminAddStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateOrUpdateStoreUserDTO;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreUserDTO;

import javax.validation.Valid;
import java.util.List;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store-user")
public class AdminStoreUserResource {
    private static final String ENTITY_NAME = "admin-store-user";
    private final Logger log = LoggerFactory.getLogger(AdminStoreUserResource.class);
    private final AdminStoreUserService adminStoreUserService;

    public AdminStoreUserResource(AdminStoreUserService adminStoreUserService) {
        this.adminStoreUserService = adminStoreUserService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdminStoreUserDTO> createStoreUser(@Valid @RequestBody AdminCreateOrUpdateStoreUserDTO payload) {
        log.debug("REST request from user [{}] to create {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreUserService.createStoreUser(payload));
    }

    /**
     * Thêm tài khoản đã có trên hệ thống vào một cửa hàng
     */
    @PostMapping("/add")
    public ResponseEntity<AdminStoreUserDTO> addStoreUser(@Valid @RequestBody AdminAddStoreUserDTO payload) {
        log.debug("REST request from user [{}] to add {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreUserService.addStoreUser(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<AdminStoreUserDTO> updateStoreUser(@Valid @RequestBody AdminCreateOrUpdateStoreUserDTO payload) {
        log.debug("REST request from user [{}] to update {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreUserService.updateStoreUser(payload));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AdminStoreUserDTO>> getListStoreUserByStoreGuid(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list {} by store : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminStoreUserService.getListStoreUserByStoreGuid(storeGuid));
    }

    @PutMapping("/toggle-account-activation/{storeUserGuid}")
    public ResponseEntity<Void> toggleAccountActivation(@PathVariable String storeUserGuid) {
        log.debug("REST request from user [{}] to toggle account activation for {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeUserGuid);
        adminStoreUserService.toggleAccountActivation(storeUserGuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Xóa tài khoản khỏi cửa hàng
     */
    @DeleteMapping("/delete/{storeUserGuid}")
    public ResponseEntity<Void> deleteStoreUser(@PathVariable String storeUserGuid) {
        log.debug("REST request from user [{}] to delete {} : {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeUserGuid);
        adminStoreUserService.deleteStoreUser(storeUserGuid);
        return ResponseEntity.noContent().build();
    }


}
