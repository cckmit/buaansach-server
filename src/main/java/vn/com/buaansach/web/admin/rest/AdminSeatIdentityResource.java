package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.store.SeatEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminSeatIdentityService;
import vn.com.buaansach.web.admin.service.AdminSeatService;
import vn.com.buaansach.web.admin.service.dto.write.AdminCreateSeatDTO;

import javax.validation.Valid;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/seat-identity")
@RequiredArgsConstructor
public class AdminSeatIdentityResource {
    private static final String ENTITY_NAME = "admin-seat-identity";
    private final Logger log = LoggerFactory.getLogger(AdminSeatIdentityResource.class);

    private final AdminSeatIdentityService adminSeatIdentityService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateAllSeatIdentity() {
        log.debug("REST request from user [{}] to update all [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME);
        adminSeatIdentityService.updateAllSeatIdentity();
        return ResponseEntity.ok().build();
    }

}
