package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreWorkShiftUserService;
import vn.com.buaansach.web.admin.service.dto.write.AdminUpdateStoreWorkShiftUserDTO;

import javax.validation.Valid;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store-work-shift-user")
@RequiredArgsConstructor
public class AdminStoreWorkShiftUserResource {
    private static final String ENTITY_NAME = "admin-store-work-shift-user";
    private final Logger log = LoggerFactory.getLogger(AdminStoreWorkShiftUserResource.class);
    private final AdminStoreWorkShiftUserService adminStoreWorkShiftUserService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateStoreWorkShiftUser(@Valid @RequestBody AdminUpdateStoreWorkShiftUserDTO payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        adminStoreWorkShiftUserService.updateStoreWorkShiftUser(payload);
        return ResponseEntity.noContent().build();
    }
}
