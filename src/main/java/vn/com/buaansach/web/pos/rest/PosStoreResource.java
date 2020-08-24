package vn.com.buaansach.web.pos.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import vn.com.buaansach.security.util.AuthoritiesConstants;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.pos.security.PosStoreSecurity;
import vn.com.buaansach.web.pos.service.PosStoreService;
import vn.com.buaansach.web.pos.service.dto.read.PosStoreDTO;
import vn.com.buaansach.web.pos.service.dto.write.PosStoreStatusChangeDTO;

import java.util.UUID;

@Secured(AuthoritiesConstants.INTERNAL_USER)
@RestController
@RequestMapping("/api/v1/pos/store")
@RequiredArgsConstructor
public class PosStoreResource {
    private final String ENTITY_NAME = "pos-store";
    private final Logger log = LoggerFactory.getLogger(PosStoreResource.class);
    private final PosStoreSecurity posStoreSecurity;
    private final PosStoreService posStoreService;

    @GetMapping("/accessible/{storeGuid}")
    public ResponseEntity<Boolean> checkAccessibility(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to get accessible [{}] by store: [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posStoreSecurity.hasPermission(UUID.fromString(storeGuid)));
    }

    @GetMapping("/get/{storeGuid}")
    public ResponseEntity<PosStoreDTO> getStore(@PathVariable String storeGuid) {
        log.debug("REST request from user [{}] to get [{}] : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(posStoreService.getStore(storeGuid));
    }

    @PutMapping("/change-status")
    public ResponseEntity<Void> changeStoreStatus(@RequestBody PosStoreStatusChangeDTO payload){
        log.debug("REST request from user [{}] to change [{}] status : [{}]", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, payload);
        posStoreService.changeStoreStatus(payload);
        return ResponseEntity.ok().build();
    }
}
