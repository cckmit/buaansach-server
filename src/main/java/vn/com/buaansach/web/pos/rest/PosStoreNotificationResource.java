package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.entity.enumeration.StoreNotificationType;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.service.PosStoreNotificationService;
import vn.com.buaansach.web.pos.service.dto.readwrite.PosStoreNotificationDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreNotificationVisibilityUpdateDTO;

import java.time.Instant;
import java.util.List;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/store-notification")
@RequiredArgsConstructor
public class PosStoreNotificationResource {
    private final String ENTITY_NAME = "pos-store-notification";
    private final Logger log = LoggerFactory.getLogger(PosStoreNotificationResource.class);
    private final PosStoreNotificationService posStoreNotificationService;

    @GetMapping("/list-by-store")
    public ResponseEntity<List<PosStoreNotificationDTO>> getListStoreNotification(@RequestParam("storeGuid") String storeGuid,
                                                                           @RequestParam("startDate") Instant startDate,
                                                                           @RequestParam(value = "type", required = false) StoreNotificationType type,
                                                                           @RequestParam(value = "hidden", required = false) Boolean hidden) {
        log.debug("REST request from user [{}] to list [{}] by store: [{}] - from [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid, startDate);
        return ResponseEntity.ok(posStoreNotificationService.getListStoreNotification(storeGuid, startDate, type, hidden));
    }

    @PutMapping("/update-status")
    public ResponseEntity<PosStoreNotificationDTO> updateStoreNotification(@RequestBody PosStoreNotificationDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to update [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        return ResponseEntity.ok(posStoreNotificationService.updateStoreNotification(payload, currentUser));
    }

    @PutMapping("/toggle-visibility")
    public ResponseEntity<Void> toggleVisibility(@RequestBody PosStoreNotificationVisibilityUpdateDTO payload) {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request from user [{}] to toggle [{}] : [{}]", currentUser, ENTITY_NAME, payload);
        posStoreNotificationService.toggleVisibility(payload, currentUser);
        return ResponseEntity.ok().build();
    }
}
