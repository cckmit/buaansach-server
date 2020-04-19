package vn.com.buaansach.web.pos.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.buaansach.web.admin.service.StoreUserSecurityService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pos/store")
public class PosStoreResource {
    private final StoreUserSecurityService storeUserSecurityService;

    public PosStoreResource(StoreUserSecurityService storeUserSecurityService) {
        this.storeUserSecurityService = storeUserSecurityService;
    }

    @GetMapping("/accessible/{storeGuid}")
    public ResponseEntity<Boolean> checkAccessibility(@PathVariable String storeGuid) {
        return ResponseEntity.ok(storeUserSecurityService.hasPermission(UUID.fromString(storeGuid)));
    }
}
