package vn.com.buaansach.web.admin.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.store.StoreWorkShiftEntity;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.AdminStoreWorkShiftService;
import vn.com.buaansach.web.admin.service.dto.read.AdminStoreWorkShiftDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Secured(AuthoritiesConstants.ADMIN)
@RestController
@RequestMapping("/api/v1/admin/store-work-shift")
@RequiredArgsConstructor
public class AdminStoreWorkShiftResource {
    private static final String ENTITY_NAME = "admin-store-work-shift";
    private final Logger log = LoggerFactory.getLogger(AdminStoreWorkShiftResource.class);
    private final AdminStoreWorkShiftService adminStoreWorkShiftService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StoreWorkShiftEntity> createStoreWorkShift(@Valid @RequestBody StoreWorkShiftEntity payload) {
        log.debug("REST request from user [{}] to create [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreWorkShiftService.createStoreWorkShift(payload));
    }

    @PutMapping("/update")
    public ResponseEntity<StoreWorkShiftEntity> updateStoreWorkShift(@Valid @RequestBody StoreWorkShiftEntity payload) {
        log.debug("REST request from user [{}] to update [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        return ResponseEntity.ok(adminStoreWorkShiftService.updateStoreWorkShift(payload));
    }

    @GetMapping("/list-by-store/{storeGuid}")
    public ResponseEntity<List<AdminStoreWorkShiftDTO>> getListStoreWorkShiftByStore(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to list [{}] by store : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(adminStoreWorkShiftService.getListStoreWorkShiftByStore(UUID.fromString(storeGuid)));
    }

    @DeleteMapping("/delete/{storeWorkShiftGuid}")
    public ResponseEntity<Void> deleteStoreWorkShift(@PathVariable String storeWorkShiftGuid) {
        log.debug("REST request from user [{}] to delete [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeWorkShiftGuid);
        adminStoreWorkShiftService.deleteStoreWorkShift(UUID.fromString(storeWorkShiftGuid));
        return ResponseEntity.noContent().build();
    }

}
