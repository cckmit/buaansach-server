package vn.com.buaansach.web.pos.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.security.util.SecurityUtils;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pos/store")
public class PosStoreResource {
    private final String ENTITY_NAME = "pos-store";
    private final Logger log = LoggerFactory.getLogger(PosStoreResource.class);
    private final StoreUserSecurityService storeUserSecurityService;

    public PosStoreResource(StoreUserSecurityService storeUserSecurityService) {
        this.storeUserSecurityService = storeUserSecurityService;
    }

    @GetMapping("/accessible/{storeGuid}")
    public ResponseEntity<Boolean> checkAccessibility(@PathVariable String storeGuid) {
        log.debug("REST request from user {} to get accessible {} by store: {}", SecurityUtils.getCurrentUserLogin(), ENTITY_NAME, storeGuid);
        return ResponseEntity.ok(storeUserSecurityService.hasPermission(UUID.fromString(storeGuid)));
    }
}
